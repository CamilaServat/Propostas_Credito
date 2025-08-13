package br.com.credit.creditproposals.presentation.controller;

import br.com.credit.creditproposals.application.service.ProposalService;
import br.com.credit.creditproposals.domain.model.Proposal;
import br.com.credit.creditproposals.presentation.dto.ProposalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/propostas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProposalController {

    private final ProposalService propostaService;

    public ProposalController(ProposalService propostaService) {
        this.propostaService = propostaService;
    }

    @Operation(summary = "Cria uma nova proposta de crédito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proposta criada com sucesso",
            content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Long> criarProposta(@Valid @RequestBody ProposalRequest request) {
        Long id = propostaService.criarProposta(
            request.getCpf(),
            request.getValorSolicitado(),
            request.getQuantidadeParcelas(),
            request.getDataSolicitacao());

        return ResponseEntity.created(URI.create("/propostas/" + id)).body(id);
    }

    @Operation(summary = "Busca uma proposta pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proposta encontrada",
            content = @Content(schema = @Schema(implementation = Proposal.class))),
        @ApiResponse(responseCode = "404", description = "Proposta não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proposal> buscarProposta(@PathVariable Long id) {
        Proposal proposta = propostaService.buscarProposta(id);
        return ResponseEntity.ok(proposta);
    }

    @Operation(summary = "Lista todas as propostas com paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de propostas retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public Page<Proposal> listarPropostas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return propostaService.listarPropostas(PageRequest.of(page, size));
    }

    @Operation(summary = "Realiza o pagamento de uma parcela de uma proposta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parcela paga com sucesso", content = @Content),
        @ApiResponse(responseCode = "400", description = "Erro no pagamento da parcela", content = @Content),
        @ApiResponse(responseCode = "404", description = "Proposta ou parcela não encontrada", content = @Content)
    })
    @PostMapping("/{id}/parcelas/{numeroParcela}/pagar")
    public ResponseEntity<Void> pagarParcela(
            @PathVariable Long id,
            @PathVariable int numeroParcela) {
            propostaService.pagarParcela(id, numeroParcela);
            return ResponseEntity.ok().build();
    }
}