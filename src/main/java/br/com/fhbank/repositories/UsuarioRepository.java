package br.com.fhbank.repositories;


import br.com.fhbank.enums.TipoUsuario;
import br.com.fhbank.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllByTipo(TipoUsuario tipo);
}
