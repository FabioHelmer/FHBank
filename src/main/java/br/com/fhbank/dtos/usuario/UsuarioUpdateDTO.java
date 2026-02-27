package br.com.fhbank.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
        @NotBlank String nome,
        @NotBlank String cpf,
        @Email String email

) {}