package br.com.credit.creditproposals.application.service;

import br.com.credit.creditproposals.domain.model.Proposal;
import br.com.credit.creditproposals.domain.repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository propostaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Simular atribuição do ID automático ao salvar uma proposta
        when(propostaRepository.save(any(Proposal.class)))
            .thenAnswer(invocation -> {
                Proposal p = invocation.getArgument(0);
                setId(p, 1L);  // seta o id via reflexão para simular o banco
                return p;
            });
    }

    // Método utilitário para setar o id privado via reflexão
    private void setId(Proposal proposta, Long id) {
        try {
            java.lang.reflect.Field idField = Proposal.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(proposta, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void criarProposta_deveSalvarERetornarId() {
        String cpf = "12345678900";
        BigDecimal valor = new BigDecimal("1500.00");
        int parcelas = 10;
        LocalDate dataSolicitacao = LocalDate.of(2025, 8, 12);

        Long id = proposalService.criarProposta(cpf, valor, parcelas, dataSolicitacao);

        assertNotNull(id);
        assertEquals(1L, id);
        verify(propostaRepository, times(1)).save(any(Proposal.class));
    }

    @Test
    void buscarProposta_deveRetornarPropostaQuandoExistir() {
        Proposal proposta = new Proposal("12345678900", new BigDecimal("1000"), 5, LocalDate.now());
        setId(proposta, 1L);

        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));

        Proposal resultado = proposalService.buscarProposta(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarProposta_deveLancarNoSuchElementExceptionQuandoNaoExistir() {
        when(propostaRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> proposalService.buscarProposta(99L));

        assertEquals("Proposta não encontrada", exception.getMessage());
    }

    @Test
    void listarPropostas_deveRetornarPaginaDePropostas() {
        Proposal p1 = new Proposal("11122233344", new BigDecimal("1000"), 5, LocalDate.now());
        Proposal p2 = new Proposal("55566677788", new BigDecimal("2000"), 8, LocalDate.now());
        setId(p1, 1L);
        setId(p2, 2L);

        List<Proposal> propostas = Arrays.asList(p1, p2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Proposal> pagina = new PageImpl<>(propostas, pageable, propostas.size());

        when(propostaRepository.findAll(pageable)).thenReturn(pagina);

        Page<Proposal> resultado = proposalService.listarPropostas(pageable);

        assertEquals(2, resultado.getContent().size());
        verify(propostaRepository, times(1)).findAll(pageable);
    }

    @Test
    void pagarParcela_deveChamarMetodoPagarParcelaESalvar() {
        Proposal proposta = spy(new Proposal("12345678900", new BigDecimal("1000"), 5, LocalDate.now()));
        setId(proposta, 1L);

        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        when(propostaRepository.save(proposta)).thenReturn(proposta);

        proposalService.pagarParcela(1L, 2);

        verify(propostaRepository, times(1)).findById(1L);
        verify(proposta, times(1)).pagarParcela(2);
        verify(propostaRepository, times(1)).save(proposta);
    }

    @Test
    void pagarParcela_deveLancarExcecaoSePropostaNaoExistir() {
        when(propostaRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> proposalService.pagarParcela(1L, 1));

        assertEquals("Proposta não encontrada", exception.getMessage());
    }
}