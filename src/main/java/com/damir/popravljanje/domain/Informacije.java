package com.damir.popravljanje.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Informacije.
 */
@Entity
@Table(name = "informacije")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "informacije")
public class Informacije implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vrsta_oglasa")
    private String vrstaOglasa;

    @Column(name = "datum_objave")
    private Instant datumObjave;

    @Column(name = "broj_pregleda")
    private Integer brojPregleda;

    @OneToOne
    @JoinColumn(unique = true)
    private Artikl artikl;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVrstaOglasa() {
        return vrstaOglasa;
    }

    public Informacije vrstaOglasa(String vrstaOglasa) {
        this.vrstaOglasa = vrstaOglasa;
        return this;
    }

    public void setVrstaOglasa(String vrstaOglasa) {
        this.vrstaOglasa = vrstaOglasa;
    }

    public Instant getDatumObjave() {
        return datumObjave;
    }

    public Informacije datumObjave(Instant datumObjave) {
        this.datumObjave = datumObjave;
        return this;
    }

    public void setDatumObjave(Instant datumObjave) {
        this.datumObjave = datumObjave;
    }

    public Integer getBrojPregleda() {
        return brojPregleda;
    }

    public Informacije brojPregleda(Integer brojPregleda) {
        this.brojPregleda = brojPregleda;
        return this;
    }

    public void setBrojPregleda(Integer brojPregleda) {
        this.brojPregleda = brojPregleda;
    }

    public Artikl getArtikl() {
        return artikl;
    }

    public Informacije artikl(Artikl artikl) {
        this.artikl = artikl;
        return this;
    }

    public void setArtikl(Artikl artikl) {
        this.artikl = artikl;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Informacije)) {
            return false;
        }
        return id != null && id.equals(((Informacije) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Informacije{" +
            "id=" + getId() +
            ", vrstaOglasa='" + getVrstaOglasa() + "'" +
            ", datumObjave='" + getDatumObjave() + "'" +
            ", brojPregleda=" + getBrojPregleda() +
            "}";
    }
}
