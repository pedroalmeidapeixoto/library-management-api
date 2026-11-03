package com.biblioteca.database;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.TipoUsuario;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.DevolucaoService;
import com.biblioteca.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Runs the Flyway migrations on a real PostgreSQL and checks the database-side rules
 * (return procedure, late fee, triggers and functions) through the application services.
 * Hibernate runs with ddl-auto=validate, so the context only starts if the migrated
 * schema matches the JPA entities.
 */
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.flyway.enabled=true",
        "spring.flyway.baseline-on-migrate=false"
})
@Testcontainers
class DatabaseIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private DevolucaoService devolucaoService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("TRUNCATE auditoria_emprestimo, multa, emprestimo, exemplar, livro, usuario RESTART IDENTITY CASCADE");
    }

    @Test
    void shouldApplyAllMigrations() {
        Integer applied = jdbc.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success", Integer.class);

        assertEquals(4, applied);
    }

    @Test
    void shouldPersistEntitiesAgainstMigratedSchema() {
        Usuario usuario = usuarioRepository.save(new Usuario("Pedro", TipoUsuario.ALUNO, "pedro@example.com"));
        Livro livro = livroRepository.save(new Livro("Dom Casmurro", 1899, "Livros do Brasil", "Romance"));

        assertNotNull(usuario.getId());
        assertNotNull(livro.getId());
        assertEquals("ALUNO", jdbc.queryForObject(
                "SELECT tipo FROM usuario WHERE id_usuario = ?", String.class, usuario.getId()));
    }

    @Test
    void shouldCreateLateFeeOfOneFiftyPerDayWhenReturnedAfterDueDate() {
        long userId = insertUser("late@example.com");
        long copyId = insertCopy(insertBook());
        long loanId = insertLoan(userId, copyId, LocalDate.now().minusDays(5));

        devolucaoService.realizarDevolucao((int) loanId);

        BigDecimal fee = jdbc.queryForObject(
                "SELECT valor FROM multa WHERE id_emprestimo = ?", BigDecimal.class, loanId);
        assertEquals(new BigDecimal("7.50"), fee);
        assertEquals("atrasado", loanStatus(loanId));
        assertEquals("disponivel", copyStatus(copyId));
        assertEquals(LocalDate.now(), jdbc.queryForObject(
                "SELECT data_devolucao_real FROM emprestimo WHERE id_emprestimo = ?", LocalDate.class, loanId));
    }

    @Test
    void shouldNotCreateFeeWhenReturnedOnTime() {
        long userId = insertUser("ontime@example.com");
        long copyId = insertCopy(insertBook());
        long loanId = insertLoan(userId, copyId, LocalDate.now().plusDays(3));

        devolucaoService.realizarDevolucao((int) loanId);

        assertEquals(0, countFees(loanId));
        assertEquals("devolvido", loanStatus(loanId));
        assertEquals("disponivel", copyStatus(copyId));
    }

    @Test
    void shouldFailToReturnUnknownLoan() {
        DataAccessException error = assertThrows(DataAccessException.class,
                () -> devolucaoService.realizarDevolucao(999));

        assertTrue(error.getMessage().contains("não existe"));
    }

    @Test
    void shouldSummarizeUserFines() {
        long userId = insertUser("fines@example.com");
        long loanId = insertLoan(userId, insertCopy(insertBook()), LocalDate.now().minusDays(2));
        devolucaoService.realizarDevolucao((int) loanId);

        Map<String, Object> summary = devolucaoService.getMultasUsuario((int) userId);

        assertEquals(1, summary.get("quantidade_multa_pendente"));
        assertEquals(0, summary.get("quantidade_multa_paga"));
        assertEquals(new BigDecimal("3.00"), summary.get("total_pendente"));
        assertEquals(3.0, devolucaoService.calcularTotalMultaPendente((int) userId));
    }

    @Test
    void shouldReportCopyAvailability() {
        long copyId = insertCopy(insertBook());
        assertTrue(devolucaoService.verificarDisponibilidade((int) copyId));

        insertLoan(insertUser("avail@example.com"), copyId, LocalDate.now().plusDays(7));

        assertFalse(devolucaoService.verificarDisponibilidade((int) copyId));
        assertFalse(devolucaoService.verificarDisponibilidade(9999));
    }

    @Test
    void shouldBorrowCopyAndWriteAuditRowWhenLoanIsCreated() {
        long userId = insertUser("loan@example.com");
        long copyId = insertCopy(insertBook());

        Emprestimo loan = emprestimoService.criarEmprestimo(userId, copyId, LocalDate.now().plusDays(7));

        assertEquals("ativo", loanStatus(loan.getIdEmprestimo()));
        assertEquals("emprestado", copyStatus(copyId));
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM auditoria_emprestimo WHERE id_emprestimo = ? AND evento = 'INSERT'",
                Integer.class, loan.getIdEmprestimo()));
    }

    @Test
    void shouldRejectLoanOfUnavailableCopy() {
        long userId = insertUser("busy@example.com");
        long copyId = insertCopy(insertBook());
        insertLoan(userId, copyId, LocalDate.now().plusDays(7));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> emprestimoService.criarEmprestimo(userId, copyId, LocalDate.now().plusDays(7)));

        assertTrue(error.getMessage().contains("não está disponível"));
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM emprestimo", Integer.class));
    }

    private long insertUser(String email) {
        return jdbc.queryForObject(
                "INSERT INTO usuario (nome, tipo, email) VALUES ('Test User', 'ALUNO', ?) RETURNING id_usuario",
                Long.class, email);
    }

    private long insertBook() {
        return jdbc.queryForObject(
                "INSERT INTO livro (titulo) VALUES ('Test Book') RETURNING id_livro", Long.class);
    }

    private long insertCopy(long bookId) {
        return jdbc.queryForObject(
                "INSERT INTO exemplar (id_livro) VALUES (?) RETURNING id_exemplar", Long.class, bookId);
    }

    /** Inserting the loan fires the triggers, so the copy ends up as 'emprestado'. */
    private long insertLoan(long userId, long copyId, LocalDate dueDate) {
        return jdbc.queryForObject(
                "INSERT INTO emprestimo (id_usuario, id_exemplar, data_devolucao_prevista) VALUES (?, ?, ?) RETURNING id_emprestimo",
                Long.class, userId, copyId, dueDate);
    }

    private String loanStatus(long loanId) {
        return jdbc.queryForObject(
                "SELECT status FROM emprestimo WHERE id_emprestimo = ?", String.class, loanId);
    }

    private String copyStatus(long copyId) {
        return jdbc.queryForObject(
                "SELECT status FROM exemplar WHERE id_exemplar = ?", String.class, copyId);
    }

    private int countFees(long loanId) {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM multa WHERE id_emprestimo = ?", Integer.class, loanId);
    }
}
