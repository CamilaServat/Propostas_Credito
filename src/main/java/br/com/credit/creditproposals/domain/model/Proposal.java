package br.com.credit.creditproposals.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CPF
    private String cpf;

    private BigDecimal valorSolicitado;
    private int quantidadeParcelas;
    private LocalDate dataSolicitacao;

    @OneToMany(mappedBy = "proposta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Installment> parcelas = new ArrayList<>();

    protected Proposal() {
        // Construtor padrão para uso do JPA
    }

    /**
     * Construtor para criar uma nova proposta de crédito
     * Gera automaticamente as parcelas com status EM_ABERTO
     */
    public Proposal(String cpf, BigDecimal valorSolicitado, int quantidadeParcelas, LocalDate dataSolicitacao) {
        this.cpf = cpf;
        this.valorSolicitado = valorSolicitado;
        this.quantidadeParcelas = quantidadeParcelas;
        this.dataSolicitacao = dataSolicitacao;
        gerarParcelas();
    }

    // Gera as parcelas com valor dividido igualmente e status inicial EM_ABERTO
    private void gerarParcelas() {
        BigDecimal valorParcela = valorSolicitado.divide(new BigDecimal(quantidadeParcelas), 2, BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i <= quantidadeParcelas; i++) {
            parcelas.add(new Installment(i, valorParcela, this));
        }
    }

    /**
     * Realiza o pagamento de uma parcela específica
     * @param numeroParcela = número da parcela a ser paga
     * @throws NoSuchElementException se a parcela não for encontrada
     * @throws IllegalStateException se a parcela já estiver paga
     */
    public void pagarParcela(int numeroParcela) {
        Installment parcela = parcelas.stream()
                .filter(p -> p.getNumero() == numeroParcela)
                .findFirst()
                .orElseThrow(() -> new java.util.NoSuchElementException("Parcela não encontrada"));
        parcela.pagar();
    }

    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    // Retorna uma cópia imutável da lista de parcelas.
    public List<Installment> getParcelas() {
        return List.copyOf(parcelas);
    }
}