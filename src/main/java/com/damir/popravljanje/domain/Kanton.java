package com.damir.popravljanje.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Kanton.
 */
@Entity
@Table(name = "kanton")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "kanton")
public class Kanton implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime_kantona")
    private String imeKantona;

    @ManyToOne
    @JsonIgnoreProperties(value = "kantons", allowSetters = true)
    private Entiteti entitet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeKantona() {
        return imeKantona;
    }

    public Kanton imeKantona(String imeKantona) {
        this.imeKantona = imeKantona;
        return this;
    }

    public void setImeKantona(String imeKantona) {
        this.imeKantona = imeKantona;
    }

    public Entiteti getEntitet() {
        return entitet;
    }

    public Kanton entitet(Entiteti entiteti) {
        this.entitet = entiteti;
        return this;
    }

    public void setEntitet(Entiteti entiteti) {
        this.entitet = entiteti;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kanton)) {
            return false;
        }
        return id != null && id.equals(((Kanton) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kanton{" +
            "id=" + getId() +
            ", imeKantona='" + getImeKantona() + "'" +
            "}";
    }
}
