package com.damir.popravljanje.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Entiteti.
 */
@Entity
@Table(name = "entiteti")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "entiteti")
public class Entiteti implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime_entiteta")
    private String imeEntiteta;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeEntiteta() {
        return imeEntiteta;
    }

    public Entiteti imeEntiteta(String imeEntiteta) {
        this.imeEntiteta = imeEntiteta;
        return this;
    }

    public void setImeEntiteta(String imeEntiteta) {
        this.imeEntiteta = imeEntiteta;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entiteti)) {
            return false;
        }
        return id != null && id.equals(((Entiteti) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entiteti{" +
            "id=" + getId() +
            ", imeEntiteta='" + getImeEntiteta() + "'" +
            "}";
    }
}
