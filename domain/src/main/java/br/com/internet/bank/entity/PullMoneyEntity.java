package br.com.internet.bank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "tb_pull_money")
public class PullMoneyEntity extends CreateUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_pull", unique = true, nullable = false)
    private Long coSeqDeposito;

    @Column(name = "num_value", nullable = false, length = 30)
    private BigDecimal numValor;

    @Column(name = "num_value_last", nullable = false, length = 30)
    private BigDecimal numValorAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_seq_client")
    @JsonBackReference
    @ToString.Exclude
    private ClientEntity client;


}