package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.EquipementRecensementRepository;
import com.dgi.gestionactifs.service.EquipementRecensementQueryService;
import com.dgi.gestionactifs.service.EquipementRecensementService;
import com.dgi.gestionactifs.service.criteria.EquipementRecensementCriteria;
import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.EquipementRecensement}.
 */
@RestController
@RequestMapping("/api/equipement-recensements")
public class EquipementRecensementResource {

    private static final Logger LOG = LoggerFactory.getLogger(EquipementRecensementResource.class);

    private static final String ENTITY_NAME = "equipementRecensement";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final EquipementRecensementService equipementRecensementService;

    private final EquipementRecensementRepository equipementRecensementRepository;

    private final EquipementRecensementQueryService equipementRecensementQueryService;

    public EquipementRecensementResource(
        EquipementRecensementService equipementRecensementService,
        EquipementRecensementRepository equipementRecensementRepository,
        EquipementRecensementQueryService equipementRecensementQueryService
    ) {
        this.equipementRecensementService = equipementRecensementService;
        this.equipementRecensementRepository = equipementRecensementRepository;
        this.equipementRecensementQueryService = equipementRecensementQueryService;
    }

    /**
     * {@code POST  /equipement-recensements} : Create a new equipementRecensement.
     *
     * @param equipementRecensementDTO the equipementRecensementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipementRecensementDTO, or with status {@code 400 (Bad Request)} if the equipementRecensement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EquipementRecensementDTO> createEquipementRecensement(
        @Valid @RequestBody EquipementRecensementDTO equipementRecensementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EquipementRecensement : {}", equipementRecensementDTO);
        if (equipementRecensementDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipementRecensement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        equipementRecensementDTO = equipementRecensementService.save(equipementRecensementDTO);
        return ResponseEntity.created(new URI("/api/equipement-recensements/" + equipementRecensementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, equipementRecensementDTO.getId().toString()))
            .body(equipementRecensementDTO);
    }

    /**
     * {@code PUT  /equipement-recensements/:id} : Updates an existing equipementRecensement.
     *
     * @param id the id of the equipementRecensementDTO to save.
     * @param equipementRecensementDTO the equipementRecensementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementRecensementDTO,
     * or with status {@code 400 (Bad Request)} if the equipementRecensementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipementRecensementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EquipementRecensementDTO> updateEquipementRecensement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EquipementRecensementDTO equipementRecensementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EquipementRecensement : {}, {}", id, equipementRecensementDTO);
        if (equipementRecensementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementRecensementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementRecensementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        equipementRecensementDTO = equipementRecensementService.update(equipementRecensementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipementRecensementDTO.getId().toString()))
            .body(equipementRecensementDTO);
    }

    /**
     * {@code PATCH  /equipement-recensements/:id} : Partial updates given fields of an existing equipementRecensement, field will ignore if it is null
     *
     * @param id the id of the equipementRecensementDTO to save.
     * @param equipementRecensementDTO the equipementRecensementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementRecensementDTO,
     * or with status {@code 400 (Bad Request)} if the equipementRecensementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipementRecensementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipementRecensementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EquipementRecensementDTO> partialUpdateEquipementRecensement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EquipementRecensementDTO equipementRecensementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EquipementRecensement partially : {}, {}", id, equipementRecensementDTO);
        if (equipementRecensementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementRecensementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementRecensementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquipementRecensementDTO> result = equipementRecensementService.partialUpdate(equipementRecensementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipementRecensementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equipement-recensements} : get all the Equipement Recensements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Equipement Recensements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EquipementRecensementDTO>> getAllEquipementRecensements(
        EquipementRecensementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EquipementRecensements by criteria: {}", criteria);

        Page<EquipementRecensementDTO> page = equipementRecensementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipement-recensements/count} : count all the equipementRecensements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEquipementRecensements(EquipementRecensementCriteria criteria) {
        LOG.debug("REST request to count EquipementRecensements by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipementRecensementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipement-recensements/:id} : get the "id" equipementRecensement.
     *
     * @param id the id of the equipementRecensementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipementRecensementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipementRecensementDTO> getEquipementRecensement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EquipementRecensement : {}", id);
        Optional<EquipementRecensementDTO> equipementRecensementDTO = equipementRecensementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipementRecensementDTO);
    }

    /**
     * {@code DELETE  /equipement-recensements/:id} : delete the "id" equipementRecensement.
     *
     * @param id the id of the equipementRecensementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipementRecensement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EquipementRecensement : {}", id);
        equipementRecensementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
