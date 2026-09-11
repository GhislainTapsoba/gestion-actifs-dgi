package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.BordereauRepository;
import com.dgi.gestionactifs.service.BordereauQueryService;
import com.dgi.gestionactifs.service.BordereauService;
import com.dgi.gestionactifs.service.criteria.BordereauCriteria;
import com.dgi.gestionactifs.service.dto.BordereauDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.Bordereau}.
 */
@RestController
@RequestMapping("/api/bordereaus")
public class BordereauResource {

    private static final Logger LOG = LoggerFactory.getLogger(BordereauResource.class);

    private static final String ENTITY_NAME = "bordereau";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final BordereauService bordereauService;

    private final BordereauRepository bordereauRepository;

    private final BordereauQueryService bordereauQueryService;

    public BordereauResource(
        BordereauService bordereauService,
        BordereauRepository bordereauRepository,
        BordereauQueryService bordereauQueryService
    ) {
        this.bordereauService = bordereauService;
        this.bordereauRepository = bordereauRepository;
        this.bordereauQueryService = bordereauQueryService;
    }

    /**
     * {@code POST  /bordereaus} : Create a new bordereau.
     *
     * @param bordereauDTO the bordereauDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bordereauDTO, or with status {@code 400 (Bad Request)} if the bordereau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BordereauDTO> createBordereau(@Valid @RequestBody BordereauDTO bordereauDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bordereau : {}", bordereauDTO);
        if (bordereauDTO.getId() != null) {
            throw new BadRequestAlertException("A new bordereau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bordereauDTO = bordereauService.save(bordereauDTO);
        return ResponseEntity.created(new URI("/api/bordereaus/" + bordereauDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bordereauDTO.getId().toString()))
            .body(bordereauDTO);
    }

    /**
     * {@code PUT  /bordereaus/:id} : Updates an existing bordereau.
     *
     * @param id the id of the bordereauDTO to save.
     * @param bordereauDTO the bordereauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bordereauDTO,
     * or with status {@code 400 (Bad Request)} if the bordereauDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bordereauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BordereauDTO> updateBordereau(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BordereauDTO bordereauDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bordereau : {}, {}", id, bordereauDTO);
        if (bordereauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bordereauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bordereauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bordereauDTO = bordereauService.update(bordereauDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bordereauDTO.getId().toString()))
            .body(bordereauDTO);
    }

    /**
     * {@code PATCH  /bordereaus/:id} : Partial updates given fields of an existing bordereau, field will ignore if it is null
     *
     * @param id the id of the bordereauDTO to save.
     * @param bordereauDTO the bordereauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bordereauDTO,
     * or with status {@code 400 (Bad Request)} if the bordereauDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bordereauDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bordereauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BordereauDTO> partialUpdateBordereau(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BordereauDTO bordereauDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bordereau partially : {}, {}", id, bordereauDTO);
        if (bordereauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bordereauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bordereauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BordereauDTO> result = bordereauService.partialUpdate(bordereauDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bordereauDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bordereaus} : get all the Bordereaus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Bordereaus in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BordereauDTO>> getAllBordereaus(
        BordereauCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Bordereaus by criteria: {}", criteria);

        Page<BordereauDTO> page = bordereauQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bordereaus/count} : count all the bordereaus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBordereaus(BordereauCriteria criteria) {
        LOG.debug("REST request to count Bordereaus by criteria: {}", criteria);
        return ResponseEntity.ok().body(bordereauQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bordereaus/:id} : get the "id" bordereau.
     *
     * @param id the id of the bordereauDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bordereauDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BordereauDTO> getBordereau(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bordereau : {}", id);
        Optional<BordereauDTO> bordereauDTO = bordereauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bordereauDTO);
    }

    /**
     * {@code DELETE  /bordereaus/:id} : delete the "id" bordereau.
     *
     * @param id the id of the bordereauDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBordereau(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bordereau : {}", id);
        bordereauService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
