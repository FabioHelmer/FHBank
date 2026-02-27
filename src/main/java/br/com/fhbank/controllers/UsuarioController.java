package br.com.fhbank.controllers;


import br.com.fhbank.dtos.usuario.UsuarioCreateDTO;
import br.com.fhbank.dtos.usuario.UsuarioResponseDTO;
import br.com.fhbank.dtos.usuario.UsuarioUpdateDTO;
import br.com.fhbank.enums.TipoUsuario;
import br.com.fhbank.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO criar(@Valid @RequestBody UsuarioCreateDTO dto) {
        return service.criarUsuario(dto);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioUpdateDTO dto
    ) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listarPorTipo(
            @PathVariable TipoUsuario tipo
    ) {
        return service.listarPorTipo(tipo);
    }


}
