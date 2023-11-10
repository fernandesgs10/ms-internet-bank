package br.com.internet.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "tb_client")
public class ClientEntity extends CreateUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_client", unique = true, nullable = false)
    private Long coSeqCliente;

    @Column(name = "nm_nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "pl_exclusive", length = 80)
    private Boolean planoExclusive;

    @Column(name = "dd_birthday", nullable = false, length = 8)
    private LocalDate dataNascimento;

    @Transient
    private BigDecimal saldo;
    @Transient
    private BigDecimal saldoAnterior;
    @Transient
    private BigDecimal saldoResidual;


    @Column(name = "nm_conta", nullable = false, length = 30)
    private String numeroConta;

}