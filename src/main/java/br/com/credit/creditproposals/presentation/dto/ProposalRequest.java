package br.com.credit.creditproposals.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.hibernate.validator.constraints.br.CPF;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProposalRequest {
    
    @Schema(description = "CPF do solicitante", example = "12345678900", required = true)
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;

    @Schema(description = "Valor solicitado para crédito (mínimo R$ 100,00)", example = "1500.00", required = true)
    @NotNull(message = "Valor solicitado é obrigatório")
    @DecimalMin(value = "100.00", message = "Valor solicitado deve ser no mínimo R$ 100,00")
    private java.math.BigDecimal valorSolicitado;

    @Schema(description = "Quantidade de parcelas (1 a 24)", example = "12", minimum = "1", maximum = "24")
    @Min(value = 1, message = "Quantidade de parcelas deve ser no mínimo 1")
    @Max(value = 24, message = "Quantidade de parcelas deve ser no máximo 24")
    private int quantidadeParcelas;

    @Schema(description = "Data da solicitação da proposta", example = "2025-08-12", required = true)
    @NotNull(message = "Data de solicitação é obrigatória")
    private LocalDate dataSolicitacao;

    public ProposalRequest() {}

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public java.math.BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(java.math.BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
}