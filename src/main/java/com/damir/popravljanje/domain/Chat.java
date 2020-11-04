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
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chat")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datum")
    private Instant datum;

    @Column(name = "postoji")
    private Boolean postoji;

    @ManyToOne
    @JsonIgnoreProperties(value = "chats", allowSetters = true)
    private Artikl artikl;

    @OneToOne(mappedBy = "chat")
    @JsonIgnore
    private Ucesnici ucesnici;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatum() {
        return datum;
    }

    public Chat datum(Instant datum) {
        this.datum = datum;
        return this;
    }

    public void setDatum(Instant datum) {
        this.datum = datum;
    }

    public Boolean isPostoji() {
        return postoji;
    }

    public Chat postoji(Boolean postoji) {
        this.postoji = postoji;
        return this;
    }

    public void setPostoji(Boolean postoji) {
        this.postoji = postoji;
    }

    public Artikl getArtikl() {
        return artikl;
    }

    public Chat artikl(Artikl artikl) {
        this.artikl = artikl;
        return this;
    }

    public void setArtikl(Artikl artikl) {
        this.artikl = artikl;
    }

    public Ucesnici getUcesnici() {
        return ucesnici;
    }

    public Chat ucesnici(Ucesnici ucesnici) {
        this.ucesnici = ucesnici;
        return this;
    }

    public void setUcesnici(Ucesnici ucesnici) {
        this.ucesnici = ucesnici;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chat)) {
            return false;
        }
        return id != null && id.equals(((Chat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", datum='" + getDatum() + "'" +
            ", postoji='" + isPostoji() + "'" +
            "}";
    }
}
