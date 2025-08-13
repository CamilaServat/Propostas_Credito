package br.com.credit.creditproposals.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa o status da parcela no sistema de propostas de crédito.
 * EM_ABERTO = indica que a parcela ainda não foi paga.
 * PAGA = indica que a parcela já foi quitada.
 */
@Schema(description = "Status da parcela no sistema de propostas de crédito", allowableValues = {"EM_ABERTO", "PAGA"})
public enum InstallmentStatus {
    EM_ABERTO,
    PAGA
}