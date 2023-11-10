package br.com.internet.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreateUpdateEntity {

    @Column(name = "nm_created", nullable = false, updatable = false)
    private String nmCreated;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public java.util.Date dtCreated;

    @Column(name = "nm_edited")
    private String nmEdited;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_updated", insertable = false)
    private java.util.Date lastUpdated;

}
