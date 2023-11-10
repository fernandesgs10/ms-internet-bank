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

@Setter
@Getter
@Entity
@Table(name = "tb_transaction")
public class TransactionEntity extends CreateUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_transaction")
    private Long coSeqTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_seq_client")
    @JsonBackReference
    @ToString.Exclude
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_seq_push")
    @JsonBackReference
    @ToString.Exclude
    private PushMoneyEntity saqueDinheiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_seq_pull")
    @JsonBackReference
    @ToString.Exclude
    private PullMoneyEntity depositarDinheiro;


}