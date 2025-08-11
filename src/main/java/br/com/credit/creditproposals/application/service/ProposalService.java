package br.com.credit.creditproposals.application.service;

import br.com.credit.creditproposals.domain.model.Proposal;
import br.com.credit.creditproposals.domain.repository.ProposalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.math.BigDecimal;

@Service
public class ProposalService {

    private final ProposalRepository propostaRepository;

    public ProposalService(ProposalRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    /**
     * Cria uma nova proposta de crédito com os dados informados,
     * gera as parcelas correspondentes e persiste no banco.
     * 
     * @param cpf = CPF do solicitante
     * @param valor = Valor solicitado para crédito
     * @param quantidadeParcelas = Número de parcelas para pagamento
     * @param dataSolicitacao = Data da solicitação da proposta
     * @return ID da proposta criada
     */
    public Long criarProposta(String cpf, BigDecimal valor, int quantidadeParcelas, LocalDate dataSolicitacao) {
        Proposal proposta = new Proposal(cpf, valor, quantidadeParcelas, dataSolicitacao);
        propostaRepository.save(proposta);
        return proposta.getId();
    }

    /**
     * Busca uma proposta pelo seu ID.
     * 
     * @param id = ID da proposta
     * @return Proposta encontrada
     * @throws NoSuchElementException se a proposta não for encontrada
     */
    public Proposal buscarProposta(Long id) {
        return propostaRepository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Proposta não encontrada"));
    }

    /**
     * Retorna uma página paginada de propostas.
     * 
     * @param pageable = Objeto que define paginação e ordenação
     * @return Página de propostas
     */
    public Page<Proposal> listarPropostas(Pageable pageable) {
        return propostaRepository.findAll(pageable);
    }

    /**
     * Marca uma parcela específica de uma proposta como PAGA.
     * 
     * @param propostaId = ID da proposta
     * @param numeroParcela = Número da parcela a ser paga
     * @throws NoSuchElementException se a proposta ou parcela não existir
     * @throws IllegalStateException se a parcela já estiver paga
     */
    public void pagarParcela(Long propostaId, int numeroParcela) {
        Proposal proposta = buscarProposta(propostaId);
        proposta.pagarParcela(numeroParcela);
        propostaRepository.save(proposta);
    }
}