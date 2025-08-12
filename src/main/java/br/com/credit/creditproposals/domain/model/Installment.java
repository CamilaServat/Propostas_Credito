package br.com.credit.creditproposals.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parcela")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número da parcela dentro da proposta
    @Column(name = "numero")
    private int numero;

    // Valor monetário da parcela
    @Column(name = "valor")
    private BigDecimal valor;

    // Status da parcela (EM_ABERTO, PAGA)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InstallmentStatus status;

    /**
     * Proposta à qual esta parcela pertence
     * Relacionamento ManyToOne para Proposal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id")
    @JsonBackReference
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