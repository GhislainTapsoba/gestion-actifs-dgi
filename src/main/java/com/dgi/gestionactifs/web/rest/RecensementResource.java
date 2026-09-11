package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.RecensementRepository;
import com.dgi.gestionactifs.service.RecensementQueryService;
import com.dgi.gestionactifs.service.RecensementService;
import com.dgi.gestionactifs.service.criteria.RecensementCriteria;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.Recensement}.
 */
@RestController
@RequestMapping("/api/recensements")
public class RecensementResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecensementResource.class);

    private static final String ENTITY_NAME = "recensement";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final RecensementService recensementService;

    private final RecensementRepository recensementRepository;

    private final RecensementQueryService recensementQueryService;

    public RecensementResource(
        RecensementService recensementService,
        RecensementRepository recensementRepository,
        RecensementQueryService recensementQueryService
    ) {
        this.recensementService = recensementService;
        this.recensementRepository = recensementRepository;
        this.recensementQueryService = recensementQueryService;
    }

    /**
     * {@code POST  /recensements} : Create a new recensement.
     *
     * @param recensementDTO the recensementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recensementDTO, or with status {@code 400 (Bad Request)} if the recensement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecensementDTO> createRecensement(@Valid @RequestBody RecensementDTO recensementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Recensement : {}", recensementDTO);
        if (recensementDTO.getId() != null) {
            throw new BadRequestAlertException("A new recensement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recensementDTO = recensementService.save(recensementDTO);
        return ResponseEntity.created(new URI("/api/recensements/" + recensementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recensementDTO.getId().toString()))
            .body(recensementDTO);
    }

    /**
     * {@code PUT  /recensements/:id} : Updates an existing recensement.
     *
     * @param id the id of the recensementDTO to save.
     * @param recensementDTO the recensementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recensementDTO,
     * or with status {@code 400 (Bad Request)} if the recensementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recensementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecensementDTO> updateRecensement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecensementDTO recensementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Recensement : {}, {}", id, recensementDTO);
        if (recensementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recensementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recensementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recensementDTO = recensementService.update(recensementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recensementDTO.getId().toString()))
            .body(recensementDTO);
    }

    /**
     * {@code PATCH  /recensements/:id} : Partial updates given fields of an existing recensement, field will ignore if it is null
     *
     * @param id the id of the recensementDTO to save.
     * @param recensementDTO the recensementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recensementDTO,
     * or with status {@code 400 (Bad Request)} if the recensementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recensementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recensementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecensementDTO> partialUpdateRecensement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecensementDTO recensementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recensement partially : {}, {}", id, recensementDTO);
        if (recensementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recensementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recensementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecensementDTO> result = recensementService.partialUpdate(recensementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recensementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recensements} : get all the Recensements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recensements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecensementDTO>> getAllRecensements(
        RecensementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Recensements by criteria: {}", criteria);

        Page<RecensementDTO> page = recensementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recensements/count} : count all the recensements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecensements(RecensementCriteria criteria) {
        LOG.debug("REST request to count Recensements by criteria: {}", criteria);
        return ResponseEntity.ok().body(recensementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recensements/:id} : get the "id" recensement.
     *
     * @param id the id of the recensementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recensementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecensementDTO> getRecensement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Recensement : {}", id);
        Optional<RecensementDTO> recensementDTO = recensementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recensementDTO);
    }

    /**
     * {@code DELETE  /recensements/:id} : delete the "id" recensement.
     *
     * @param id the id of the recensementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecensement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Recensement : {}", id);
        recensementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
