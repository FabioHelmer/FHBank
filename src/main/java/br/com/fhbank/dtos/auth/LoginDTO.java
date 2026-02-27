package br.com.fhbank.dtos.auth;

public record LoginDTO(
        String email,
        String senha
) {}