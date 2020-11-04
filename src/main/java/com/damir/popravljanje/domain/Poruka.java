package com.damir.popravljanje.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Poruka.
 */
@Entity
@Table(name = "poruka")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "poruka")
public class Poruka implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "datum")
    private Instant datum;

    @Column(name = "postoji")
    private Boolean postoji;

    @OneToOne
    @JoinColumn(unique = true)
    private DodatniInfoUser dodatniInfoUser;

    @ManyToOne
    @JsonIgnoreProperties(value = "porukas", allowSetters = true)
    private Chat chat;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Poruka text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getDatum() {
        return datum;
    }

    public Poruka datum(Instant datum) {
        this.datum = datum;
        return this;
    }

    public void setDatum(Instant datum) {
        this.datum = datum;
    }

    public Boolean isPostoji() {
        return postoji;
    }

    public Poruka postoji(Boolean postoji) {
        this.postoji = postoji;
        return this;
    }

    public void setPostoji(Boolean postoji) {
        this.postoji = postoji;
    }

    public DodatniInfoUser getDodatniInfoUser() {
        return dodatniInfoUser;
    }

    public Poruka dodatniInfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniInfoUser = dodatniInfoUser;
        return this;
    }

    public void setDodatniInfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniInfoUser = dodatniInfoUser;
    }

    public Chat getChat() {
        return chat;
    }

    public Poruka chat(Chat chat) {
        this.chat = chat;
        return this;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poruka)) {
            return false;
        }
        return id != null && id.equals(((Poruka) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poruka{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", datum='" + getDatum() + "'" +
            ", postoji='" + isPostoji() + "'" +
            "}";
    }
}
