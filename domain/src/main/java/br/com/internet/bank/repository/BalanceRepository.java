package br.com.internet.bank.repository;

import br.com.internet.bank.entity.BalanceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

    @Query("select b " +
            "from BalanceEntity b join fetch b.client c " +
            "where c.coSeqCliente = :coSeqCliente " +
            "order by b.coSeqBalance desc")
    Page<BalanceEntity> findBalanceLastValue(@Param("coSeqCliente") Long coSeqCliente, Pageable pageable);

    @Query("select b " +
            "from BalanceEntity b join fetch b.client c " +
            "where c.coSeqCliente = :coSeqCliente " +
            "order by b.coSeqBalance asc")
    Page<BalanceEntity> findBalanceFirstValue(@Param("coSeqCliente") Long coSeqCliente, Pageable pageable);





}
