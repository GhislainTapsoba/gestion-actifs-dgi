package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.PanneRepository;
import com.dgi.gestionactifs.service.PanneQueryService;
import com.dgi.gestionactifs.service.PanneService;
import com.dgi.gestionactifs.service.criteria.PanneCriteria;
import com.dgi.gestionactifs.service.dto.PanneDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.Panne}.
 */
@RestController
@RequestMapping("/api/pannes")
public class PanneResource {

    private static final Logger LOG = LoggerFactory.getLogger(PanneResource.class);

    private static final String ENTITY_NAME = "panne";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final PanneService panneService;

    private final PanneRepository panneRepository;

    private final PanneQueryService panneQueryService;

    public PanneResource(PanneService panneService, PanneRepository panneRepository, PanneQueryService panneQueryService) {
        this.panneService = panneService;
        this.panneRepository = panneRepository;
        this.panneQueryService = panneQueryService;
    }

    /**
     * {@code POST  /pannes} : Create a new panne.
     *
     * @param panneDTO the panneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new panneDTO, or with status {@code 400 (Bad Request)} if the panne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PanneDTO> createPanne(@Valid @RequestBody PanneDTO panneDTO) throws URISyntaxException {
        LOG.debug("REST request to save Panne : {}", panneDTO);
        if (panneDTO.getId() != null) {
            throw new BadRequestAlertException("A new panne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        panneDTO = panneService.save(panneDTO);
        return ResponseEntity.created(new URI("/api/pannes/" + panneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, panneDTO.getId().toString()))
            .body(panneDTO);
    }

    /**
     * {@code PUT  /pannes/:id} : Updates an existing panne.
     *
     * @param id the id of the panneDTO to save.
     * @param panneDTO the panneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panneDTO,
     * or with status {@code 400 (Bad Request)} if the panneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the panneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PanneDTO> updatePanne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PanneDTO panneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Panne : {}, {}", id, panneDTO);
        if (panneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        panneDTO = panneService.update(panneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, panneDTO.getId().toString()))
            .body(panneDTO);
    }

    /**
     * {@code PATCH  /pannes/:id} : Partial updates given fields of an existing panne, field will ignore if it is null
     *
     * @param id the id of the panneDTO to save.
     * @param panneDTO the panneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panneDTO,
     * or with status {@code 400 (Bad Request)} if the panneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the panneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the panneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PanneDTO> partialUpdatePanne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PanneDTO panneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Panne partially : {}, {}", id, panneDTO);
        if (panneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PanneDTO> result = panneService.partialUpdate(panneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, panneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pannes} : get all the Pannes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pannes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PanneDTO>> getAllPannes(
        PanneCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Pannes by criteria: {}", criteria);

        Page<PanneDTO> page = panneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pannes/count} : count all the pannes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPannes(PanneCriteria criteria) {
        LOG.debug("REST request to count Pannes by criteria: {}", criteria);
        return ResponseEntity.ok().body(panneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pannes/:id} : get the "id" panne.
     *
     * @param id the id of the panneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the panneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PanneDTO> getPanne(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Panne : {}", id);
        Optional<PanneDTO> panneDTO = panneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(panneDTO);
    }

    /**
     * {@code DELETE  /pannes/:id} : delete the "id" panne.
     *
     * @param id the id of the panneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanne(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Panne : {}", id);
        panneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
