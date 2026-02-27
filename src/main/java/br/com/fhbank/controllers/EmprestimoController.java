package br.com.fhbank.controllers;

import br.com.fhbank.dtos.emprestimo.EmprestimoRecusaDTO;
import br.com.fhbank.dtos.emprestimo.EmprestimoResponseDTO;
import br.com.fhbank.dtos.emprestimo.EmprestimoSolicitacaoDTO;
import br.com.fhbank.models.Usuario;
import br.com.fhbank.services.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService service;

    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    /* USUÁRIO */
    @PostMapping("/solicitar")
    public EmprestimoResponseDTO solicitar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody EmprestimoSolicitacaoDTO dto
    ) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());
        return service.solicitar(usuarioId, dto);
    }

    /* ADMIN */
    @GetMapping("/pendentes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmprestimoResponseDTO> pendentes() {
        return service.listarPendentes();
    }

    @PostMapping("/{id}/aprovar")
    @PreAuthorize("hasRole('ADMIN')")
    public void aprovar(@PathVariable UUID id,
                        @AuthenticationPrincipal Jwt jwt) {
        UUID idUsuarioAdmin = UUID.fromString(jwt.getSubject());
        service.aprovar(id, idUsuarioAdmin);
    }

    @PostMapping("/{id}/recusar")
    @PreAuthorize("hasRole('ADMIN')")
    public void recusar(
            @PathVariable UUID id,
            @RequestBody @Valid EmprestimoRecusaDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID idUsuarioAdmin = UUID.fromString(jwt.getSubject());
        service.recusar(id, idUsuarioAdmin, dto.motivo());
    }
}
