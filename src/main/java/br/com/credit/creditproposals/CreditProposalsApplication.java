package br.com.credit.creditproposals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe principal da aplicação Spring Boot para o sistema de propostas de crédito
 * Responsável por iniciar o contexto da aplicação
 */
@SpringBootApplication
public class CreditProposalsApplication {

    private static final Logger logger = LoggerFactory.getLogger(CreditProposalsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CreditProposalsApplication.class, args);
        logger.info("Aplicação CreditProposals iniciada com sucesso!");
    }
}