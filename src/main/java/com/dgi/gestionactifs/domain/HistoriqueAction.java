package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.TypeMouvement;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HistoriqueAction.
 */
@Entity
@Table(name = "historique_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueAction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_action", nullable = false)
    private ZonedDateTime dateAction;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_action", nullable = false)
    private TypeMouvement typeAction;

    @Column(name = "entite_ciblee")
    private String entiteCiblee;

    @Column(name = "ancienne_valeur")
    private String ancienneValeur;

    @Column(name = "nouvelle_valeur")
    private String nouvelleValeur;

    @ManyToOne(optional = false)
    @NotNull
    private User utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateAction() {
        return this.dateAction;
    }

    public HistoriqueAction dateAction(ZonedDateTime dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public TypeMouvement getTypeAction() {
        return this.typeAction;
    }

    public HistoriqueAction typeAction(TypeMouvement typeAction) {
        this.setTypeAction(typeAction);
        return this;
    }

    public void setTypeAction(TypeMouvement typeAction) {
        this.typeAction = typeAction;
    }

    public String getEntiteCiblee() {
        return this.entiteCiblee;
    }

    public HistoriqueAction entiteCiblee(String entiteCiblee) {
        this.setEntiteCiblee(entiteCiblee);
        return this;
    }

    public void setEntiteCiblee(String entiteCiblee) {
        this.entiteCiblee = entiteCiblee;
    }

    public String getAncienneValeur() {
        return this.ancienneValeur;
    }

    public HistoriqueAction ancienneValeur(String ancienneValeur) {
        this.setAncienneValeur(ancienneValeur);
        return this;
    }

    public void setAncienneValeur(String ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public String getNouvelleValeur() {
        return this.nouvelleValeur;
    }

    public HistoriqueAction nouvelleValeur(String nouvelleValeur) {
        this.setNouvelleValeur(nouvelleValeur);
        return this;
    }

    public void setNouvelleValeur(String nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public User getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(User user) {
        this.utilisateur = user;
    }

    public HistoriqueAction utilisateur(User user) {
        this.setUtilisateur(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueAction)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueAction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueAction{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", typeAction='" + getTypeAction() + "'" +
            ", entiteCiblee='" + getEntiteCiblee() + "'" +
            ", ancienneValeur='" + getAncienneValeur() + "'" +
            ", nouvelleValeur='" + getNouvelleValeur() + "'" +
            "}";
    }
}
