package br.com.fhbank.controllers;

import br.com.fhbank.dtos.conta.ContaCreateDTO;
import br.com.fhbank.dtos.conta.ContaResponseDTO;
import br.com.fhbank.dtos.conta.ContaUpdateDTO;
import br.com.fhbank.dtos.usuario.UsuarioResponseDTO;
import br.com.fhbank.enums.TipoConta;
import br.com.fhbank.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService service;

    public ContaController(ContaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ContaResponseDTO criar(@Valid @RequestBody ContaCreateDTO dto) {
        return service.criarConta(dto);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ContaResponseDTO atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ContaUpdateDTO dto
    ) {
        return service.atualizar(id, dto);
    }

    @GetMapping("/{id}")
    public ContaResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaResponseDTO> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaResponseDTO> listarPorUsuario(
            @PathVariable UUID usuarioId
    ) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaResponseDTO> listarPorTipo(
            @PathVariable TipoConta tipo
    ) {
        return service.listarPorTipo(tipo);
    }
}