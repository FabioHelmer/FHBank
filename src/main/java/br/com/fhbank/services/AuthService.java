package br.com.fhbank.services;

import br.com.fhbank.dtos.auth.LoginDTO;
import br.com.fhbank.dtos.usuario.UsuarioDTO;
import br.com.fhbank.exception.UnauthorizedError;
import br.com.fhbank.models.Usuario;
import br.com.fhbank.repositories.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO autenticar(LoginDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() ->
                        new UnauthorizedError("Usuário ou senha inválidos")
                );

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new UnauthorizedError("Usuário ou senha inválidos");
        }

        return new UsuarioDTO(
                usuario.getId().toString(),
                usuario.getTipo().name()
        );
    }
}