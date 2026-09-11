package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.TransfertActif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertActifDTO implements Serializable {

    private Long id;

    private String observation;

    @NotNull
    private TransfertDTO transfert;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public TransfertDTO getTransfert() {
        return transfert;
    }

    public void setTransfert(TransfertDTO transfert) {
        this.transfert = transfert;
    }

    public ActifDTO getActif() {
        return actif;
    }

    public void setActif(ActifDTO actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfertActifDTO)) {
            return false;
        }

        TransfertActifDTO transfertActifDTO = (TransfertActifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transfertActifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertActifDTO{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            ", transfert=" + getTransfert() +
            ", actif=" + getActif() +
            "}";
    }
}
