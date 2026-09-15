package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.RapportRepository;
import com.dgi.gestionactifs.service.RapportService;
import com.dgi.gestionactifs.service.dto.RapportDTO;
import com.dgi.gestionactifs.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dgi.gestionactifs.domain.Rapport}.
 */
@RestController
@RequestMapping("/api/rapports")
public class RapportResource {

    private static final Logger LOG = LoggerFactory.getLogger(RapportResource.class);

    private static final String ENTITY_NAME = "rapport";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final RapportService rapportService;

    private final RapportRepository rapportRepository;

    public RapportResource(RapportService rapportService, RapportRepository rapportRepository) {
        this.rapportService = rapportService;
        this.rapportRepository = rapportRepository;
    }

    /**
     * {@code POST  /rapports} : Create a new rapport.
     *
     * @param rapportDTO the rapportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapportDTO, or with status {@code 400 (Bad Request)} if the rapport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE', 'ROLE_AGENT')")
    public ResponseEntity<RapportDTO> createRapport(@Valid @RequestBody RapportDTO rapportDTO) throws URISyntaxException {
        LOG.debug("REST request to save Rapport : {}", rapportDTO);
        if (rapportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rapport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rapportDTO = rapportService.save(rapportDTO);
        return ResponseEntity.created(new URI("/api/rapports/" + rapportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rapportDTO.getId().toString()))
            .body(rapportDTO);
    }

    /**
     * {@code PUT  /rapports/:id} : Updates an existing rapport.
     *
     * @param id the id of the rapportDTO to save.
     * @param rapportDTO the rapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportDTO,
     * or with status {@code 400 (Bad Request)} if the rapportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE', 'ROLE_AGENT')")
    public ResponseEntity<RapportDTO> updateRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RapportDTO rapportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Rapport : {}, {}", id, rapportDTO);
        if (rapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rapportDTO = rapportService.update(rapportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportDTO.getId().toString()))
            .body(rapportDTO);
    }

    /**
     * {@code PATCH  /rapports/:id} : Partial updates given fields of an existing rapport, field will ignore if it is null
     *
     * @param id the id of the rapportDTO to save.
     * @param rapportDTO the rapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportDTO,
     * or with status {@code 400 (Bad Request)} if the rapportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rapportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE', 'ROLE_AGENT')")
    public ResponseEntity<RapportDTO> partialUpdateRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RapportDTO rapportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Rapport partially : {}, {}", id, rapportDTO);
        if (rapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RapportDTO> result = Optional.empty();

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rapports} : get all the rapports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapports in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE', 'ROLE_AGENT', 'ROLE_TECHNICIEN')")
    public ResponseEntity<List<RapportDTO>> getAllRapports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String typeRapport,
        @RequestParam(required = false) String generePar
    ) {
        LOG.debug("REST request to get a page of Rapports");
        Page<RapportDTO> page;

        if (typeRapport != null) {
            page = rapportService.findByTypeRapport(typeRapport, pageable);
        } else if (generePar != null) {
            page = rapportService.findByGenerePar(generePar, pageable);
        } else {
            page = rapportService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rapports/:id} : get the "id" rapport.
     *
     * @param id the id of the rapportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE', 'ROLE_AGENT', 'ROLE_TECHNICIEN')")
    public ResponseEntity<RapportDTO> getRapport(@PathVariable Long id) {
        LOG.debug("REST request to get Rapport : {}", id);
        RapportDTO rapportDTO = rapportService.findOne(id);
        if (rapportDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rapportDTO);
    }

    /**
     * {@code DELETE  /rapports/:id} : delete the "id" rapport.
     *
     * @param id the id of the rapportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE')")
    public ResponseEntity<Void> deleteRapport(@PathVariable Long id) {
        LOG.debug("REST request to delete Rapport : {}", id);
        rapportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
