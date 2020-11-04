package com.damir.popravljanje.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ucesnici.
 */
@Entity
@Table(name = "ucesnici")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ucesnici")
public class Ucesnici implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Chat chat;

    @OneToMany(mappedBy = "ucesnici")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<DodatniInfoUser> dodatniInfoUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public Ucesnici chat(Chat chat) {
        this.chat = chat;
        return this;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Set<DodatniInfoUser> getDodatniInfoUsers() {
        return dodatniInfoUsers;
    }

    public Ucesnici dodatniInfoUsers(Set<DodatniInfoUser> dodatniInfoUsers) {
        this.dodatniInfoUsers = dodatniInfoUsers;
        return this;
    }

    public Ucesnici addDodatniInfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniInfoUsers.add(dodatniInfoUser);
        dodatniInfoUser.setUcesnici(this);
        return this;
    }

    public Ucesnici removeDodatniInfoUser(DodatniInfoUser dodatniInfoUser) {
        this.dodatniInfoUsers.remove(dodatniInfoUser);
        dodatniInfoUser.setUcesnici(null);
        return this;
    }

    public void setDodatniInfoUsers(Set<DodatniInfoUser> dodatniInfoUsers) {
        this.dodatniInfoUsers = dodatniInfoUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ucesnici)) {
            return false;
        }
        return id != null && id.equals(((Ucesnici) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ucesnici{" +
            "id=" + getId() +
            "}";
    }
}
