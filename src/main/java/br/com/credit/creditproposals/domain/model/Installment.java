package br.com.credit.creditproposals.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "parcela")
@Schema(description = "Representa uma parcela de uma proposta de crédito")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da parcela", example = "1")
    private Long id;

    @Column(name = "numero")
    @Schema(description = "Número sequencial da parcela dentro da proposta", example = "1")
    private int numero;

    @Column(name = "valor")
    @Schema(description = "Valor monetário da parcela", example = "100.00")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "Status atual da parcela", example = "EM_ABERTO", allowableValues = {"EM_ABERTO", "PAGA"})
    private InstallmentStatus status;

    /**
     * Proposta à qual esta parcela pertence
     * Relacionamento ManyToOne para Proposal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id")
    @JsonBackReference
    @Schema(hidden = true) 
    private Proposal proposta;

    protected Installment() {
        // Construtor padrão para uso do JPA 
    }

    /**
     * Construtor para criar uma parcela com número, valor e proposta associada
     * Inicializa o status como EM_ABERTO.
     */
    public Installment(int numero, BigDecimal valor, Proposal proposta) {
        this.numero = numero;
        this.valor = valor;
        this.status = InstallmentStatus.EM_ABERTO;
        this.proposta = proposta;
    }

    /**
     * Marca a parcela como paga.
     * Lança IllegalStateException se a parcela já estiver paga
     */
    public void pagar() {
        if (this.isPaga()) {
            throw new IllegalStateException("Parcela já está paga");
        }

        this.status = InstallmentStatus.PAGA;
    }

    public boolean isPaga() {
        return this.status == InstallmentStatus.PAGA;
    }

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public InstallmentStatus getStatus() {
        return status;
    }

    public Proposal getProposta() {
        return proposta;
    }
}