package br.com.fhbank.repositories;

import br.com.fhbank.enums.StatusEmprestimo;
import br.com.fhbank.models.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findAllByStatus(StatusEmprestimo status);

    List<Emprestimo> findAllByContaUsuarioId(UUID usuarioId);
}
