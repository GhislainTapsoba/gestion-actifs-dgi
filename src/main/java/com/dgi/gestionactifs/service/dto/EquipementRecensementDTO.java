package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.EtatMateriel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.EquipementRecensement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipementRecensementDTO implements Serializable {

    private Long id;

    @NotNull
    private EtatMateriel etatConstate;

    @NotNull
    private LocalDate dateConstat;

    private String emplacementConstate;

    private Boolean anomalieConstatee;

    @NotNull
    private RecensementDTO recensement;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtatMateriel getEtatConstate() {
        return etatConstate;
    }

    public void setEtatConstate(EtatMateriel etatConstate) {
        this.etatConstate = etatConstate;
    }

    public LocalDate getDateConstat() {
        return dateConstat;
    }

    public void setDateConstat(LocalDate dateConstat) {
        this.dateConstat = dateConstat;
    }

    public String getEmplacementConstate() {
        return emplacementConstate;
    }

    public void setEmplacementConstate(String emplacementConstate) {
        this.emplacementConstate = emplacementConstate;
    }

    public Boolean getAnomalieConstatee() {
        return anomalieConstatee;
    }

    public void setAnomalieConstatee(Boolean anomalieConstatee) {
        this.anomalieConstatee = anomalieConstatee;
    }

    public RecensementDTO getRecensement() {
        return recensement;
    }

    public void setRecensement(RecensementDTO recensement) {
        this.recensement = recensement;
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
        if (!(o instanceof EquipementRecensementDTO)) {
            return false;
        }

        EquipementRecensementDTO equipementRecensementDTO = (EquipementRecensementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipementRecensementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementRecensementDTO{" +
            "id=" + getId() +
            ", etatConstate='" + getEtatConstate() + "'" +
            ", dateConstat='" + getDateConstat() + "'" +
            ", emplacementConstate='" + getEmplacementConstate() + "'" +
            ", anomalieConstatee='" + getAnomalieConstatee() + "'" +
            ", recensement=" + getRecensement() +
            ", actif=" + getActif() +
            "}";
    }
}
