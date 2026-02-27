package br.com.fhbank.models;

import br.com.fhbank.enums.StatusEmprestimo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Emprestimo {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_conta", nullable = false)
    private Conta conta;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJuros; // ex: 2.50 %

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private Integer parcelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEmprestimo status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataResposta;

    @ManyToOne
    @JoinColumn(name = "id_aprovado_por")
    private Usuario aprovadoPor;

    @Column(length = 255)
    private String motivoRecusa;

    @Version
    private Long version;


    @OneToMany(mappedBy = "emprestimo")
    private List<Transacao> transacoes = new ArrayList<>();

    @PrePersist
    void prePersist() {
        this.dataSolicitacao = LocalDateTime.now();
        this.status = StatusEmprestimo.PENDENTE;
    }
}
