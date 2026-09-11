package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.InterventionRepository;
import com.dgi.gestionactifs.service.InterventionQueryService;
import com.dgi.gestionactifs.service.InterventionService;
import com.dgi.gestionactifs.service.criteria.InterventionCriteria;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dgi.gestionactifs.domain.Intervention}.
 */
@RestController
@RequestMapping("/api/interventions")
public class InterventionResource {

    private static final Logger LOG = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final InterventionService interventionService;

    private final InterventionRepository interventionRepository;

    private final InterventionQueryService interventionQueryService;

    public InterventionResource(
        InterventionService interventionService,
        InterventionRepository interventionRepository,
        InterventionQueryService interventionQueryService
    ) {
        this.interventionService = interventionService;
        this.interventionRepository = interventionRepository;
        this.interventionQueryService = interventionQueryService;
    }

    /**
     * {@code POST  /interventions} : Create a new intervention.
     *
     * @param interventionDTO the interventionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interventionDTO, or with status {@code 400 (Bad Request)} if the intervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InterventionDTO> createIntervention(@Valid @RequestBody InterventionDTO interventionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Intervention : {}", interventionDTO);
        if (interventionDTO.getId() != null) {
            throw new BadRequestAlertException("A new intervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        interventionDTO = interventionService.save(interventionDTO);
        return ResponseEntity.created(new URI("/api/interventions/" + interventionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, interventionDTO.getId().toString()))
            .body(interventionDTO);
    }

    /**
     * {@code PUT  /interventions/:id} : Updates an existing intervention.
     *
     * @param id the id of the interventionDTO to save.
     * @param interventionDTO the interventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionDTO,
     * or with status {@code 400 (Bad Request)} if the interventionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InterventionDTO> updateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterventionDTO interventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Intervention : {}, {}", id, interventionDTO);
        if (interventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        interventionDTO = interventionService.update(interventionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionDTO.getId().toString()))
            .body(interventionDTO);
    }

    /**
     * {@code PATCH  /interventions/:id} : Partial updates given fields of an existing intervention, field will ignore if it is null
     *
     * @param id the id of the interventionDTO to save.
     * @param interventionDTO the interventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionDTO,
     * or with status {@code 400 (Bad Request)} if the interventionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the interventionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the interventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterventionDTO> partialUpdateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterventionDTO interventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Intervention partially : {}, {}", id, interventionDTO);
        if (interventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterventionDTO> result = interventionService.partialUpdate(interventionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interventions} : get all the Interventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Interventions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InterventionDTO>> getAllInterventions(
        InterventionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Interventions by criteria: {}", criteria);

        Page<InterventionDTO> page = interventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interventions/count} : count all the interventions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInterventions(InterventionCriteria criteria) {
        LOG.debug("REST request to count Interventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(interventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interventions/:id} : get the "id" intervention.
     *
     * @param id the id of the interventionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interventionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InterventionDTO> getIntervention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Intervention : {}", id);
        Optional<InterventionDTO> interventionDTO = interventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interventionDTO);
    }

    /**
     * {@code DELETE  /interventions/:id} : delete the "id" intervention.
     *
     * @param id the id of the interventionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Intervention : {}", id);
        interventionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
