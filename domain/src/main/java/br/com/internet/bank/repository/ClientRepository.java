package br.com.internet.bank.repository;

import br.com.internet.bank.entity.ClientEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c")
    Page<ClientEntity> findClientAll(Pageable pageable);

    @Query("select c from ClientEntity c where c.nome = :nome and c.numeroConta = :numeroConta")
    Optional<ClientEntity> findClientByNomeAndNumeroConta(@Param("nome") String nome, @Param("numeroConta") String numeroConta);


}
