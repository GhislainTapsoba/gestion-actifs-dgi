package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.TransfertActifRepository;
import com.dgi.gestionactifs.service.TransfertActifQueryService;
import com.dgi.gestionactifs.service.TransfertActifService;
import com.dgi.gestionactifs.service.criteria.TransfertActifCriteria;
import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.TransfertActif}.
 */
@RestController
@RequestMapping("/api/transfert-actifs")
public class TransfertActifResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertActifResource.class);

    private static final String ENTITY_NAME = "transfertActif";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final TransfertActifService transfertActifService;

    private final TransfertActifRepository transfertActifRepository;

    private final TransfertActifQueryService transfertActifQueryService;

    public TransfertActifResource(
        TransfertActifService transfertActifService,
        TransfertActifRepository transfertActifRepository,
        TransfertActifQueryService transfertActifQueryService
    ) {
        this.transfertActifService = transfertActifService;
        this.transfertActifRepository = transfertActifRepository;
        this.transfertActifQueryService = transfertActifQueryService;
    }

    /**
     * {@code POST  /transfert-actifs} : Create a new transfertActif.
     *
     * @param transfertActifDTO the transfertActifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transfertActifDTO, or with status {@code 400 (Bad Request)} if the transfertActif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransfertActifDTO> createTransfertActif(@Valid @RequestBody TransfertActifDTO transfertActifDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TransfertActif : {}", transfertActifDTO);
        if (transfertActifDTO.getId() != null) {
            throw new BadRequestAlertException("A new transfertActif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transfertActifDTO = transfertActifService.save(transfertActifDTO);
        return ResponseEntity.created(new URI("/api/transfert-actifs/" + transfertActifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transfertActifDTO.getId().toString()))
            .body(transfertActifDTO);
    }

    /**
     * {@code PUT  /transfert-actifs/:id} : Updates an existing transfertActif.
     *
     * @param id the id of the transfertActifDTO to save.
     * @param transfertActifDTO the transfertActifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfertActifDTO,
     * or with status {@code 400 (Bad Request)} if the transfertActifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transfertActifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransfertActifDTO> updateTransfertActif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransfertActifDTO transfertActifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransfertActif : {}, {}", id, transfertActifDTO);
        if (transfertActifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfertActifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfertActifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transfertActifDTO = transfertActifService.update(transfertActifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transfertActifDTO.getId().toString()))
            .body(transfertActifDTO);
    }

    /**
     * {@code PATCH  /transfert-actifs/:id} : Partial updates given fields of an existing transfertActif, field will ignore if it is null
     *
     * @param id the id of the transfertActifDTO to save.
     * @param transfertActifDTO the transfertActifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfertActifDTO,
     * or with status {@code 400 (Bad Request)} if the transfertActifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transfertActifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transfertActifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransfertActifDTO> partialUpdateTransfertActif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransfertActifDTO transfertActifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransfertActif partially : {}, {}", id, transfertActifDTO);
        if (transfertActifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfertActifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfertActifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransfertActifDTO> result = transfertActifService.partialUpdate(transfertActifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transfertActifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transfert-actifs} : get all the Transfert Actifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Transfert Actifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransfertActifDTO>> getAllTransfertActifs(
        TransfertActifCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransfertActifs by criteria: {}", criteria);

        Page<TransfertActifDTO> page = transfertActifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transfert-actifs/count} : count all the transfertActifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransfertActifs(TransfertActifCriteria criteria) {
        LOG.debug("REST request to count TransfertActifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transfertActifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transfert-actifs/:id} : get the "id" transfertActif.
     *
     * @param id the id of the transfertActifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transfertActifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransfertActifDTO> getTransfertActif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransfertActif : {}", id);
        Optional<TransfertActifDTO> transfertActifDTO = transfertActifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transfertActifDTO);
    }

    /**
     * {@code DELETE  /transfert-actifs/:id} : delete the "id" transfertActif.
     *
     * @param id the id of the transfertActifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfertActif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransfertActif : {}", id);
        transfertActifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
