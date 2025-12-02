package com.biblioteca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class DevolucaoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void realizarDevolucao(Integer idEmprestimo) {
        // Chamando a PROCEDURE
        String sql = "CALL prc_realizar_devolucao(?)";
        jdbcTemplate.update(sql, idEmprestimo);
    }

    // Método antigo (manter para compatibilidade)
    public Double calcularTotalMultaPendente(Integer idUsuario) {
        String sql = "SELECT fn_calcular_total_multa_pendente(?)";
        Double total = jdbcTemplate.queryForObject(sql, Double.class, idUsuario);
        return total != null ? total : 0.0;
    }

    // Método para função fn_multas_usuario
    public Map<String, Object> getMultasUsuario(Integer idUsuario) {
        String sql = "SELECT * FROM fn_multas_usuario(?)";

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, idUsuario);

        // Converter BIGINT para Integer se necessário
        if (result.get("quantidade_multa_pendente") instanceof Long) {
            Long pendente = (Long) result.get("quantidade_multa_pendente");
            Long paga = (Long) result.get("quantidade_multa_paga");

            // Converter para Integer (se os valores couberem em Integer)
            result.put("quantidade_multa_pendente", pendente.intValue());
            result.put("quantidade_multa_paga", paga.intValue());
        }

        return result;
    }

    // Método para função fn_exemplar_disponivel
    public Boolean verificarDisponibilidade(Integer idExemplar) {
        String sql = "SELECT fn_exemplar_disponivel(?)";
        Boolean disponivel = jdbcTemplate.queryForObject(sql, Boolean.class, idExemplar);
        return disponivel != null ? disponivel : false;
    }

}