package com.damir.popravljanje.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Lokacija.
 */
@Entity
@Table(name = "lokacija")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "lokacija")
public class Lokacija implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "postanski_broj")
    private String postanskiBroj;

    @Column(name = "grad")
    private String grad;

    @ManyToOne
    @JsonIgnoreProperties(value = "lokacijas", allowSetters = true)
    private Kanton kanton;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresa() {
        return adresa;
    }

    public Lokacija adresa(String adresa) {
        this.adresa = adresa;
        return this;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public Lokacija postanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
        return this;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    public String getGrad() {
        return grad;
    }

    public Lokacija grad(String grad) {
        this.grad = grad;
        return this;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Kanton getKanton() {
        return kanton;
    }

    public Lokacija kanton(Kanton kanton) {
        this.kanton = kanton;
        return this;
    }

    public void setKanton(Kanton kanton) {
        this.kanton = kanton;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lokacija)) {
            return false;
        }
        return id != null && id.equals(((Lokacija) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lokacija{" +
            "id=" + getId() +
            ", adresa='" + getAdresa() + "'" +
            ", postanskiBroj='" + getPostanskiBroj() + "'" +
            ", grad='" + getGrad() + "'" +
            "}";
    }
}
