package br.com.credit.creditproposals.presentation.controller;

import br.com.credit.creditproposals.CreditProposalsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CreditProposalsApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProposalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void criarPropostaSucesso() throws Exception {
        String propostaJson = """
            {
              "cpf": "78858021088",
              "valorSolicitado": 1500.00,
              "quantidadeParcelas": 12,
              "dataSolicitacao": "2025-08-12"
            }
            """;

        mockMvc.perform(post("/propostas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(propostaJson))
            .andExpect(status().isCreated())
            .andExpect(content().string(not(emptyString())))
            .andExpect(result -> {
                String responseBody = result.getResponse().getContentAsString();
                Long id = Long.valueOf(responseBody);
                assertNotNull(id);
            });
    }

    @Test
    void criarPropostaBadRequest() throws Exception {
        String propostaJson = """
            {
              "cpf": "123",
              "valorSolicitado": 1500.00,
              "quantidadeParcelas": 12,
              "dataSolicitacao": "2025-08-12"
            }
            """;

        mockMvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(propostaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPropostaSucesso() throws Exception {
        String propostaJson = """
            {
              "cpf": "57958645015",
              "valorSolicitado": 1200.00,
              "quantidadeParcelas": 10,
              "dataSolicitacao": "2025-08-12"
            }
            """;

        var result = mockMvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(propostaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String id = jsonResponse.replaceAll("[^0-9]", "");

        mockMvc.perform(get("/propostas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is("57958645015")))
                .andExpect(jsonPath("$.parcelas", not(empty())));
    }

    @Test
    void buscarPropostaNotFound() throws Exception {
        mockMvc.perform(get("/propostas/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPropostasRetornarPagina() throws Exception {
        mockMvc.perform(get("/propostas?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void pagamentoParcelaSucesso() throws Exception {
        String propostaJson = """
        {
          "cpf": "94722767092",
          "valorSolicitado": 1000.00,
          "quantidadeParcelas": 5,
          "dataSolicitacao": "2025-08-12"
        }
        """;

        var result = mockMvc.perform(post("/propostas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(propostaJson))
            .andExpect(status().isCreated())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String id = jsonResponse.replaceAll("[^0-9]", "");

        mockMvc.perform(post("/propostas/" + id + "/parcelas/1/pagar"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/propostas/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.parcelas[0].status", is("PAGA")));
    }

    @Test
    void pagamentoParcelaNotFound() throws Exception {
        mockMvc.perform(post("/propostas/99999/parcelas/1/pagamento"))
                .andExpect(status().isNotFound());
    }
}