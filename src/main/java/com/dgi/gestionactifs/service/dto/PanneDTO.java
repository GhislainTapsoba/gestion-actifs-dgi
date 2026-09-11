package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutPanne;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Panne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PanneDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private LocalDate dateDeclaration;

    @NotNull
    private StatutPanne statutPanne;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public StatutPanne getStatutPanne() {
        return statutPanne;
    }

    public void setStatutPanne(StatutPanne statutPanne) {
        this.statutPanne = statutPanne;
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
        if (!(o instanceof PanneDTO)) {
            return false;
        }

        PanneDTO panneDTO = (PanneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, panneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanneDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", statutPanne='" + getStatutPanne() + "'" +
            ", actif=" + getActif() +
            "}";
    }
}
