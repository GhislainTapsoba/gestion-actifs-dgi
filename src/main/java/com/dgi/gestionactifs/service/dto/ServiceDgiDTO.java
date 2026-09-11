package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.ServiceDgi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceDgiDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomService;

    private String chefService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getChefService() {
        return chefService;
    }

    public void setChefService(String chefService) {
        this.chefService = chefService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceDgiDTO)) {
            return false;
        }

        ServiceDgiDTO serviceDgiDTO = (ServiceDgiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceDgiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceDgiDTO{" +
            "id=" + getId() +
            ", nomService='" + getNomService() + "'" +
            ", chefService='" + getChefService() + "'" +
            "}";
    }
}
