package com.dgi.gestionactifs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceDgi.
 */
@Entity
@Table(name = "service_dgi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceDgi implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_service", nullable = false)
    private String nomService;

    @Column(name = "chef_service")
    private String chefService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceDgi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomService() {
        return this.nomService;
    }

    public ServiceDgi nomService(String nomService) {
        this.setNomService(nomService);
        return this;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getChefService() {
        return this.chefService;
    }

    public ServiceDgi chefService(String chefService) {
        this.setChefService(chefService);
        return this;
    }

    public void setChefService(String chefService) {
        this.chefService = chefService;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceDgi)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceDgi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceDgi{" +
            "id=" + getId() +
            ", nomService='" + getNomService() + "'" +
            ", chefService='" + getChefService() + "'" +
            "}";
    }
}
