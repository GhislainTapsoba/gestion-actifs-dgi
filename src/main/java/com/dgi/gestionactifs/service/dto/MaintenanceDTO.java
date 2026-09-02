package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutMaintenance;
import com.dgi.gestionactifs.domain.enumeration.TypeMaintenance;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Maintenance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeMaintenance typeMaintenance;

    private LocalDate datePanne;

    @NotNull
    private StatutMaintenance statut;

    @Lob
    private String compteRendu;

    private LocalDate dateCloture;

    private UserDTO technicien;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMaintenance getTypeMaintenance() {
        return typeMaintenance;
    }

    public void setTypeMaintenance(TypeMaintenance typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
    }

    public LocalDate getDatePanne() {
        return datePanne;
    }

    public void setDatePanne(LocalDate datePanne) {
        this.datePanne = datePanne;
    }

    public StatutMaintenance getStatut() {
        return statut;
    }

    public void setStatut(StatutMaintenance statut) {
        this.statut = statut;
    }

    public String getCompteRendu() {
        return compteRendu;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public UserDTO getTechnicien() {
        return technicien;
    }

    public void setTechnicien(UserDTO technicien) {
        this.technicien = technicien;
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
        if (!(o instanceof MaintenanceDTO)) {
            return false;
        }

        MaintenanceDTO maintenanceDTO = (MaintenanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintenanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceDTO{" +
            "id=" + getId() +
            ", typeMaintenance='" + getTypeMaintenance() + "'" +
            ", datePanne='" + getDatePanne() + "'" +
            ", statut='" + getStatut() + "'" +
            ", compteRendu='" + getCompteRendu() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", technicien=" + getTechnicien() +
            ", actif=" + getActif() +
            "}";
    }
}
