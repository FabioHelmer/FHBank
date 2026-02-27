package br.com.fhbank.controllers;

import br.com.fhbank.dtos.transacoes.DepositoCreateDTO;
import br.com.fhbank.dtos.transacoes.TransacaoResponseDTO;
import br.com.fhbank.dtos.transacoes.TransferenciaCreateDTO;
import br.com.fhbank.dtos.usuario.UsuarioCreateDTO;
import br.com.fhbank.models.Transacao;
import br.com.fhbank.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/deposito")
    @ResponseStatus(HttpStatus.OK)
    public void deposito(@RequestBody DepositoCreateDTO dto) {
        service.depositar(dto);
    }

    @PostMapping("/saque/{contaId}")
    @ResponseStatus(HttpStatus.OK)
    public void saque(@PathVariable UUID contaId,
                      @RequestParam BigDecimal valor) {
        service.sacar(contaId, valor);
    }

    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.OK)
    public void transferencia(@RequestBody TransferenciaCreateDTO transferenciaDTO) {
        service.transferir(transferenciaDTO);
    }

    @GetMapping("/extrato/{contaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransacaoResponseDTO> extrato(@PathVariable UUID contaId) {
        return service.extrato(contaId);
    }
}
