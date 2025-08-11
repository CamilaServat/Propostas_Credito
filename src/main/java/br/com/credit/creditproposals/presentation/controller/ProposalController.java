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

@RestController
@RequestMapping("/propostas")
public class ProposalController {

    private final ProposalService propostaService;

    public ProposalController(ProposalService propostaService) {
        this.propostaService = propostaService;
    }

    /**
     * Cria uma nova proposta de crédito
     * @param request = Dados da proposta a ser criada
     * @return ID da proposta criada
     */
    @PostMapping
    public ResponseEntity<Long> criarProposta(@Valid @RequestBody ProposalRequest request) {
        Long id = propostaService.criarProposta(
            request.getCpf(),
            request.getValorSolicitado(),
            request.getQuantidadeParcelas(),
            request.getDataSolicitacao());

        return ResponseEntity.created(URI.create("/propostas/" + id)).body(id);
    }

    /**
     * Busca uma proposta pelo seu ID
     * @param id = ID da proposta
     * @return Proposta encontrada ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proposal> buscarProposta(@PathVariable Long id) {
        Proposal proposta = propostaService.buscarProposta(id);
        return ResponseEntity.ok(proposta);
    }

    /**
     * Lista todas as propostas paginadas
     * @param page = Número da página (padrão 0)
     * @param size = Tamanho da página (padrão 10)
     * @return Página com propostas
     */
    @GetMapping
    public Page<Proposal> listarPropostas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return propostaService.listarPropostas(PageRequest.of(page, size));
    }

    /**
     * Realiza o pagamento de uma parcela de uma proposta
     * @param id = ID da proposta
     * @param numeroParcela = Número da parcela a ser paga
     * @return 200 se pagamento OK, 400 em caso de erro
     */
    @PostMapping("/{id}/parcelas/{numeroParcela}/pagar")
    public ResponseEntity<Void> pagarParcela(
            @PathVariable Long id,
            @PathVariable int numeroParcela) {
            propostaService.pagarParcela(id, numeroParcela);
            return ResponseEntity.ok().build();
    }
}