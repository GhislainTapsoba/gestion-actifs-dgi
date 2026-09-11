package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutPanne;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Panne.
 */
@Entity
@Table(name = "panne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Panne implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_declaration", nullable = false)
    private LocalDate dateDeclaration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_panne", nullable = false)
    private StatutPanne statutPanne;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Panne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Panne description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeclaration() {
        return this.dateDeclaration;
    }

    public Panne dateDeclaration(LocalDate dateDeclaration) {
        this.setDateDeclaration(dateDeclaration);
        return this;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public StatutPanne getStatutPanne() {
        return this.statutPanne;
    }

    public Panne statutPanne(StatutPanne statutPanne) {
        this.setStatutPanne(statutPanne);
        return this;
    }

    public void setStatutPanne(StatutPanne statutPanne) {
        this.statutPanne = statutPanne;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public Panne actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Panne)) {
            return false;
        }
        return getId() != null && getId().equals(((Panne) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Panne{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", statutPanne='" + getStatutPanne() + "'" +
            "}";
    }
}
