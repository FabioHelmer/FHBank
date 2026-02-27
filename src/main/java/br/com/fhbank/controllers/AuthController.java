package br.com.fhbank.controllers;

import java.time.Instant;

import br.com.fhbank.dtos.auth.LoginDTO;
import br.com.fhbank.dtos.auth.TokenDTO;
import br.com.fhbank.dtos.usuario.UsuarioDTO;
import br.com.fhbank.services.AuthService;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final AuthService authService;

    public AuthController(JwtEncoder jwtEncoder,
                          AuthService authService) {
        this.jwtEncoder = jwtEncoder;
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO dto) {

        UsuarioDTO usuario = authService.autenticar(dto);

        Instant now = Instant.now();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwtClaimsSet.builder()
                                .issuer("fhbank")
                                .subject(usuario.id())
                                .claim("role", usuario.tipo())
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(3600))
                                .build()
                )
        ).getTokenValue();

        return new TokenDTO(token);
    }
}