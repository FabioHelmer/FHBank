package br.com.fhbank.dtos.emprestimo;

import java.math.BigDecimal;

public record EmprestimoSolicitacaoDTO(
        BigDecimal valorSolicitado,
        Integer parcelas
) {}
