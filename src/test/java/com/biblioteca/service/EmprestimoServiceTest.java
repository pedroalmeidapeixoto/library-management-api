package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.TipoUsuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private EmprestimoService service;

    private final LocalDate dueDate = LocalDate.of(2026, 12, 31);

    @Test
    void shouldCreateLoanLinkingUserAndCopy() {
        Usuario usuario = new Usuario("Pedro", TipoUsuario.ALUNO, "pedro@example.com");
        Exemplar exemplar = new Exemplar(4, "Prateleira D4");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(exemplarRepository.findById(10L)).thenReturn(Optional.of(exemplar));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(inv -> inv.getArgument(0));

        Emprestimo created = service.criarEmprestimo(1L, 10L, dueDate);

        ArgumentCaptor<Emprestimo> captor = ArgumentCaptor.forClass(Emprestimo.class);
        verify(emprestimoRepository).save(captor.capture());
        Emprestimo saved = captor.getValue();
        assertSame(usuario, saved.getUsuario());
        assertSame(exemplar, saved.getExemplar());
        assertEquals(dueDate, saved.getDataPrevistaDevolucao());
        assertSame(saved, created);
    }

    @Test
    void shouldRejectLoanWhenUserDoesNotExist() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> service.criarEmprestimo(1L, 10L, dueDate));

        assertTrue(error.getMessage().contains("1"));
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    void shouldRejectLoanWhenCopyDoesNotExist() {
        Usuario usuario = new Usuario("Pedro", TipoUsuario.ALUNO, "pedro@example.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(exemplarRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> service.criarEmprestimo(1L, 10L, dueDate));

        assertTrue(error.getMessage().contains("10"));
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        Emprestimo emprestimo = new Emprestimo(new Usuario(), new Exemplar(), dueDate);
        emprestimo.setStatus("ativo");
        when(emprestimoRepository.findById(5L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        service.atualizarEmprestimo(5L, null, LocalDate.of(2026, 12, 20));

        assertEquals("ativo", emprestimo.getStatus());
        assertEquals(LocalDate.of(2026, 12, 20), emprestimo.getDataDevolucaoReal());
    }

    @Test
    void shouldUpdateStatusWithoutTouchingReturnDate() {
        Emprestimo emprestimo = new Emprestimo(new Usuario(), new Exemplar(), dueDate);
        when(emprestimoRepository.findById(5L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        service.atualizarEmprestimo(5L, "atrasado", null);

        assertEquals("atrasado", emprestimo.getStatus());
        assertNull(emprestimo.getDataDevolucaoReal());
    }

    @Test
    void shouldThrowWhenLoanIsNotFound() {
        when(emprestimoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void shouldDeleteExistingLoan() {
        when(emprestimoRepository.existsById(5L)).thenReturn(true);

        service.deletarEmprestimo(5L);

        verify(emprestimoRepository).deleteById(5L);
    }

    @Test
    void shouldNotDeleteMissingLoan() {
        when(emprestimoRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deletarEmprestimo(99L));

        verify(emprestimoRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldDelegateQueriesByUserAndStatus() {
        List<Emprestimo> byUser = List.of(new Emprestimo());
        List<Emprestimo> byStatus = List.of(new Emprestimo(), new Emprestimo());
        when(emprestimoRepository.findByUsuario_Id(1L)).thenReturn(byUser);
        when(emprestimoRepository.findByStatus("ativo")).thenReturn(byStatus);

        assertSame(byUser, service.buscarPorUsuario(1L));
        assertSame(byStatus, service.buscarPorStatus("ativo"));
    }
}
