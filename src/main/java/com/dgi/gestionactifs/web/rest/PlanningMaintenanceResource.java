package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.PlanningMaintenanceRepository;
import com.dgi.gestionactifs.service.PlanningMaintenanceService;
import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.PlanningMaintenance}.
 */
@RestController
@RequestMapping("/api/planning-maintenances")
public class PlanningMaintenanceResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanningMaintenanceResource.class);

    private static final String ENTITY_NAME = "planningMaintenance";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final PlanningMaintenanceService planningMaintenanceService;

    private final PlanningMaintenanceRepository planningMaintenanceRepository;

    public PlanningMaintenanceResource(
        PlanningMaintenanceService planningMaintenanceService,
        PlanningMaintenanceRepository planningMaintenanceRepository
    ) {
        this.planningMaintenanceService = planningMaintenanceService;
        this.planningMaintenanceRepository = planningMaintenanceRepository;
    }

    /**
     * {@code POST  /planning-maintenances} : Create a new planningMaintenance.
     *
     * @param planningMaintenanceDTO the planningMaintenanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planningMaintenanceDTO, or with status {@code 400 (Bad Request)} if the planningMaintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanningMaintenanceDTO> createPlanningMaintenance(
        @Valid @RequestBody PlanningMaintenanceDTO planningMaintenanceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PlanningMaintenance : {}", planningMaintenanceDTO);
        if (planningMaintenanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new planningMaintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planningMaintenanceDTO = planningMaintenanceService.save(planningMaintenanceDTO);
        return ResponseEntity.created(new URI("/api/planning-maintenances/" + planningMaintenanceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planningMaintenanceDTO.getId().toString()))
            .body(planningMaintenanceDTO);
    }

    /**
     * {@code PUT  /planning-maintenances/:id} : Updates an existing planningMaintenance.
     *
     * @param id the id of the planningMaintenanceDTO to save.
     * @param planningMaintenanceDTO the planningMaintenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planningMaintenanceDTO,
     * or with status {@code 400 (Bad Request)} if the planningMaintenanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planningMaintenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanningMaintenanceDTO> updatePlanningMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanningMaintenanceDTO planningMaintenanceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlanningMaintenance : {}, {}", id, planningMaintenanceDTO);
        if (planningMaintenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planningMaintenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningMaintenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planningMaintenanceDTO = planningMaintenanceService.update(planningMaintenanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planningMaintenanceDTO.getId().toString()))
            .body(planningMaintenanceDTO);
    }

    /**
     * {@code PATCH  /planning-maintenances/:id} : Partial updates given fields of an existing planningMaintenance, field will ignore if it is null
     *
     * @param id the id of the planningMaintenanceDTO to save.
     * @param planningMaintenanceDTO the planningMaintenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planningMaintenanceDTO,
     * or with status {@code 400 (Bad Request)} if the planningMaintenanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planningMaintenanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planningMaintenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanningMaintenanceDTO> partialUpdatePlanningMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanningMaintenanceDTO planningMaintenanceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlanningMaintenance partially : {}, {}", id, planningMaintenanceDTO);
        if (planningMaintenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planningMaintenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningMaintenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanningMaintenanceDTO> result = planningMaintenanceService.partialUpdate(planningMaintenanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planningMaintenanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /planning-maintenances} : get all the Planning Maintenances.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Planning Maintenances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanningMaintenanceDTO>> getAllPlanningMaintenances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PlanningMaintenances");
        Page<PlanningMaintenanceDTO> page;
        if (eagerload) {
            page = planningMaintenanceService.findAllWithEagerRelationships(pageable);
        } else {
            page = planningMaintenanceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planning-maintenances/:id} : get the "id" planningMaintenance.
     *
     * @param id the id of the planningMaintenanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planningMaintenanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanningMaintenanceDTO> getPlanningMaintenance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlanningMaintenance : {}", id);
        Optional<PlanningMaintenanceDTO> planningMaintenanceDTO = planningMaintenanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningMaintenanceDTO);
    }

    /**
     * {@code DELETE  /planning-maintenances/:id} : delete the "id" planningMaintenance.
     *
     * @param id the id of the planningMaintenanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanningMaintenance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlanningMaintenance : {}", id);
        planningMaintenanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
