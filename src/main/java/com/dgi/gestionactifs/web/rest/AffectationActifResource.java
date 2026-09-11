package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.AffectationActifRepository;
import com.dgi.gestionactifs.service.AffectationActifQueryService;
import com.dgi.gestionactifs.service.AffectationActifService;
import com.dgi.gestionactifs.service.criteria.AffectationActifCriteria;
import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.AffectationActif}.
 */
@RestController
@RequestMapping("/api/affectation-actifs")
public class AffectationActifResource {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationActifResource.class);

    private static final String ENTITY_NAME = "affectationActif";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final AffectationActifService affectationActifService;

    private final AffectationActifRepository affectationActifRepository;

    private final AffectationActifQueryService affectationActifQueryService;

    public AffectationActifResource(
        AffectationActifService affectationActifService,
        AffectationActifRepository affectationActifRepository,
        AffectationActifQueryService affectationActifQueryService
    ) {
        this.affectationActifService = affectationActifService;
        this.affectationActifRepository = affectationActifRepository;
        this.affectationActifQueryService = affectationActifQueryService;
    }

    /**
     * {@code POST  /affectation-actifs} : Create a new affectationActif.
     *
     * @param affectationActifDTO the affectationActifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectationActifDTO, or with status {@code 400 (Bad Request)} if the affectationActif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AffectationActifDTO> createAffectationActif(@Valid @RequestBody AffectationActifDTO affectationActifDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AffectationActif : {}", affectationActifDTO);
        if (affectationActifDTO.getId() != null) {
            throw new BadRequestAlertException("A new affectationActif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        affectationActifDTO = affectationActifService.save(affectationActifDTO);
        return ResponseEntity.created(new URI("/api/affectation-actifs/" + affectationActifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, affectationActifDTO.getId().toString()))
            .body(affectationActifDTO);
    }

    /**
     * {@code PUT  /affectation-actifs/:id} : Updates an existing affectationActif.
     *
     * @param id the id of the affectationActifDTO to save.
     * @param affectationActifDTO the affectationActifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationActifDTO,
     * or with status {@code 400 (Bad Request)} if the affectationActifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationActifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AffectationActifDTO> updateAffectationActif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AffectationActifDTO affectationActifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AffectationActif : {}, {}", id, affectationActifDTO);
        if (affectationActifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationActifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationActifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        affectationActifDTO = affectationActifService.update(affectationActifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationActifDTO.getId().toString()))
            .body(affectationActifDTO);
    }

    /**
     * {@code PATCH  /affectation-actifs/:id} : Partial updates given fields of an existing affectationActif, field will ignore if it is null
     *
     * @param id the id of the affectationActifDTO to save.
     * @param affectationActifDTO the affectationActifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationActifDTO,
     * or with status {@code 400 (Bad Request)} if the affectationActifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affectationActifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectationActifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffectationActifDTO> partialUpdateAffectationActif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AffectationActifDTO affectationActifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AffectationActif partially : {}, {}", id, affectationActifDTO);
        if (affectationActifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationActifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationActifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffectationActifDTO> result = affectationActifService.partialUpdate(affectationActifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationActifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affectation-actifs} : get all the Affectation Actifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Affectation Actifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AffectationActifDTO>> getAllAffectationActifs(
        AffectationActifCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AffectationActifs by criteria: {}", criteria);

        Page<AffectationActifDTO> page = affectationActifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affectation-actifs/count} : count all the affectationActifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAffectationActifs(AffectationActifCriteria criteria) {
        LOG.debug("REST request to count AffectationActifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(affectationActifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /affectation-actifs/:id} : get the "id" affectationActif.
     *
     * @param id the id of the affectationActifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectationActifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AffectationActifDTO> getAffectationActif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AffectationActif : {}", id);
        Optional<AffectationActifDTO> affectationActifDTO = affectationActifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectationActifDTO);
    }

    /**
     * {@code DELETE  /affectation-actifs/:id} : delete the "id" affectationActif.
     *
     * @param id the id of the affectationActifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAffectationActif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AffectationActif : {}", id);
        affectationActifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
