package br.com.internet.bank.repository;

import br.com.internet.bank.entity.PullMoneyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PullMoneyRepository extends JpaRepository<PullMoneyEntity, Long> {

    @Query("select p from PullMoneyEntity p where p.client.coSeqCliente = :coSeqCliente")
    List<PullMoneyEntity> findPullByCoSeqClient(@Param("coSeqCliente") Long coSeqCliente);

    @Query("select b " +
            "from PullMoneyEntity b join fetch b.client c " +
            "where c.coSeqCliente = :coSeqCliente " +
            "order by b.coSeqDeposito desc")
    Page<PullMoneyEntity> findPullMoeyLastValue(@Param("coSeqCliente") Long coSeqCliente, Pageable pageable);

}
