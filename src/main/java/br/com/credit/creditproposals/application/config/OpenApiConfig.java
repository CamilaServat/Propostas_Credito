package br.com.credit.creditproposals.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI para documentação da API utilizando Swagger
 * Define título, versão, descrição, informações de contato e licença da API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Propostas de Crédito")
                .version("1.0")
                .description("Documentação da API de Propostas")
                .contact(new Contact().name("Camila Eduarda Servat").email("servatcamilaeduarda@gmail.com"))
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}