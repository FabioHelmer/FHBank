package br.com.fhbank.repositories;

import br.com.fhbank.enums.TipoConta;
import br.com.fhbank.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {

    boolean existsByUsuarioId(UUID idUsuario);
    Optional<Conta> findByUsuarioId(UUID idUsuario);

    List<Conta> findAllByUsuarioId(UUID idUsuario);

    List<Conta> findAllByTipo(TipoConta tipo);

}