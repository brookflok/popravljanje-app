package com.damir.popravljanje.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A JavnoPitanje.
 */
@Entity
@Table(name = "javno_pitanje")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "javnopitanje")
public class JavnoPitanje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pitanje")
    private String pitanje;

    @Column(name = "datum")
    private Instant datum;

    @Column(name = "prikaz")
    private Boolean prikaz;

    @ManyToOne
    @JsonIgnoreProperties(value = "javnoPitanjes", allowSetters = true)
    private DodatniInfoUser dodatniinfoUser;

    @ManyToOne
    @JsonIgnoreProperties(value = "javnoPitanjes", allowSetters = true)
    private Artikl artikl;

    @OneToOne(mappedBy = "javnoPitanje")
    @JsonIgnore
    private OdgovorNaJavnoPitanje odgovorNaJavnoPitanje;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPitanje() {
        return pitanje;
    }

    public JavnoPitanje pitanje(String pitanje) {
        this.pitanje = pitanje;
        return this;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public Instant getDatum() {
        return datum;
    }

    public JavnoPitanje datum(Instant datum) {
        this.datum = datum;
        return this;
    }

    public void setDatum(Instant datum) {
        this.datum = datum;
    }

    public Boolean isPrikaz() {
        return prikaz;
    }

    public JavnoPitanje prikaz(Boolean prikaz) {
        this.prikaz = prikaz;
        return this;
    }

    public void setPrikaz(Boolean prikaz) {
        this.prikaz = prikaz;
    }

    public DodatniInfoUser getDodatniinfoUser() {
        return dodatniinfoUser;
    }

    public JavnoPitanje dodatniinfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniinfoUser = dodatniInfoUser;
        return this;
    }

    public void setDodatniinfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniinfoUser = dodatniInfoUser;
    }

    public Artikl getArtikl() {
        return artikl;
    }

    public JavnoPitanje artikl(Artikl artikl) {
        this.artikl = artikl;
        return this;
    }

    public void setArtikl(Artikl artikl) {
        this.artikl = artikl;
    }

    public OdgovorNaJavnoPitanje getOdgovorNaJavnoPitanje() {
        return odgovorNaJavnoPitanje;
    }

    public JavnoPitanje odgovorNaJavnoPitanje(OdgovorNaJavnoPitanje odgovorNaJavnoPitanje) {
        this.odgovorNaJavnoPitanje = odgovorNaJavnoPitanje;
        return this;
    }

    public void setOdgovorNaJavnoPitanje(OdgovorNaJavnoPitanje odgovorNaJavnoPitanje) {
        this.odgovorNaJavnoPitanje = odgovorNaJavnoPitanje;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JavnoPitanje)) {
            return false;
        }
        return id != null && id.equals(((JavnoPitanje) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JavnoPitanje{" +
            "id=" + getId() +
            ", pitanje='" + getPitanje() + "'" +
            ", datum='" + getDatum() + "'" +
            ", prikaz='" + isPrikaz() + "'" +
            "}";
    }
}
