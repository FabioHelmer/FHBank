package br.com.fhbank.dtos.usuario;

import br.com.fhbank.enums.TipoUsuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String cpf,
        String nome,
        TipoUsuario tipo
) {}
