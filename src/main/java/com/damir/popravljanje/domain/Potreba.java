package com.damir.popravljanje.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Potreba.
 */
@Entity
@Table(name = "potreba")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "potreba")
public class Potreba implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cijena_min")
    private Double cijenaMin;

    @Column(name = "cijena_max")
    private Double cijenaMax;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCijenaMin() {
        return cijenaMin;
    }

    public Potreba cijenaMin(Double cijenaMin) {
        this.cijenaMin = cijenaMin;
        return this;
    }

    public void setCijenaMin(Double cijenaMin) {
        this.cijenaMin = cijenaMin;
    }

    public Double getCijenaMax() {
        return cijenaMax;
    }

    public Potreba cijenaMax(Double cijenaMax) {
        this.cijenaMax = cijenaMax;
        return this;
    }

    public void setCijenaMax(Double cijenaMax) {
        this.cijenaMax = cijenaMax;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Potreba)) {
            return false;
        }
        return id != null && id.equals(((Potreba) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Potreba{" +
            "id=" + getId() +
            ", cijenaMin=" + getCijenaMin() +
            ", cijenaMax=" + getCijenaMax() +
            "}";
    }
}
