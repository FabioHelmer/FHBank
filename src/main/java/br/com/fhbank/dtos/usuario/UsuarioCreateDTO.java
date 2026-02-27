package br.com.fhbank.dtos.usuario;

import br.com.fhbank.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCreateDTO(

        @NotBlank
        String cpf,

        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha,

        @NotNull
        TipoUsuario tipo

) {
}
