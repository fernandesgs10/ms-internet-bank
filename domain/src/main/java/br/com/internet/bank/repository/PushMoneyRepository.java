package br.com.internet.bank.repository;

import br.com.internet.bank.entity.PushMoneyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PushMoneyRepository extends JpaRepository<PushMoneyEntity, Long> {

    @Query("select p from PushMoneyEntity p where p.client.coSeqCliente = :coSeqCliente")
    List<PushMoneyEntity> findPushByCoSeqClient(@Param("coSeqCliente") Long coSeqCliente);


}
