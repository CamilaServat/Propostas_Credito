package br.com.credit.creditproposals.domain.model;

/**
 * Representa o status da parcela no sistema de propostas de crédito.
 * EM_ABERTO = indica que a parcela ainda não foi paga.
 * PAGA = indica que a parcela já foi quitada.
 */
public enum InstallmentStatus {
    EM_ABERTO,
    PAGA
}