package br.com.fhbank.repositories;

import br.com.fhbank.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findAllByContaIdOrderByDataDesc(UUID contaId);
}
