package br.com.fhbank.dtos.emprestimo;

import br.com.fhbank.enums.StatusEmprestimo;

import java.math.BigDecimal;
import java.util.UUID;

public record EmprestimoResponseDTO(
        UUID id,
        BigDecimal valorSolicitado,
        BigDecimal valorTotal,
        BigDecimal taxaJuros,
        Integer parcelas,
        StatusEmprestimo status
) {}
