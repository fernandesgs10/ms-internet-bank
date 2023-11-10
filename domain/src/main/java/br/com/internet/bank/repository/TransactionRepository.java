package br.com.internet.bank.repository;

import br.com.internet.bank.entity.TransactionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("select t " +
            "from TransactionEntity t join t.client c " +
            "where TO_CHAR(c.dtCreated,'YYYY-MM-DD') = :dtCreated ")
    Page<TransactionEntity> findTransactionByDtCreated(@Param("dtCreated") Date dtCreated, Pageable pageable);

        @Query("select t " +
            "from TransactionEntity t join t.client c join t.depositarDinheiro s " +
            "where s.numValor >= :numValor")
    Page<TransactionEntity> findTransactionPullMoneyByNumValue(@Param("numValor") BigDecimal numValor, Pageable pageable);

    @Query("select t " +
            "from TransactionEntity t join t.client c join t.saqueDinheiro s " +
            "where s.numValor >= :numValor")
    Page<TransactionEntity> findTransactionPushMoneyByNumValue(@Param("numValor") BigDecimal numValor, Pageable pageable);




}
