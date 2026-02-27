package br.com.fhbank.models;

import br.com.fhbank.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "cpf")
        }
)
@Builder
@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Usuario {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipo;

    /* =========================
       RELACIONAMENTOS
       ========================= */

    // Conta é dona da FK (usuario_id)
    @OneToOne(mappedBy = "usuario")
    private Conta conta;

    // Empréstimos aprovados por este usuário (ADMIN)
    @OneToMany(mappedBy = "aprovadoPor")
    private List<Emprestimo> emprestimosAprovados = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Version
    private Long version;

    @PrePersist
    void prePersist() {
        this.criadoEm = Instant.now();
    }

}
