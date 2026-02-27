package br.com.fhbank.models;

import br.com.fhbank.enums.TipoTransacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import java.util.UUID;

@Entity
@Table(
        name = "transacao",
        indexes = {
                @Index(name = "idx_transacao_conta_data", columnList = "id_conta, data")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transacao {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_conta", nullable = false)
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoTransacao tipo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoAnterior;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoPosterior;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;

    @Version
    private Long version;

    @PrePersist
    void prePersist() {
        this.data = LocalDateTime.now();
    }

}