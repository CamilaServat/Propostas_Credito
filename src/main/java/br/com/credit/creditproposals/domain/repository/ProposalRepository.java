package br.com.credit.creditproposals.domain.repository;

import br.com.credit.creditproposals.domain.model.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade Proposal
 * Permite operações CRUD e paginação de propostas de crédito
 */
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Page<Proposal> findAll(Pageable pageable);
}