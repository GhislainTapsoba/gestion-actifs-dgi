package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.ServiceDgiRepository;
import com.dgi.gestionactifs.service.ServiceDgiQueryService;
import com.dgi.gestionactifs.service.ServiceDgiService;
import com.dgi.gestionactifs.service.criteria.ServiceDgiCriteria;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.ServiceDgi}.
 */
@RestController
@RequestMapping("/api/service-dgis")
public class ServiceDgiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDgiResource.class);

    private static final String ENTITY_NAME = "serviceDgi";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final ServiceDgiService serviceDgiService;

    private final ServiceDgiRepository serviceDgiRepository;

    private final ServiceDgiQueryService serviceDgiQueryService;

    public ServiceDgiResource(
        ServiceDgiService serviceDgiService,
        ServiceDgiRepository serviceDgiRepository,
        ServiceDgiQueryService serviceDgiQueryService
    ) {
        this.serviceDgiService = serviceDgiService;
        this.serviceDgiRepository = serviceDgiRepository;
        this.serviceDgiQueryService = serviceDgiQueryService;
    }

    /**
     * {@code POST  /service-dgis} : Create a new serviceDgi.
     *
     * @param serviceDgiDTO the serviceDgiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceDgiDTO, or with status {@code 400 (Bad Request)} if the serviceDgi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceDgiDTO> createServiceDgi(@Valid @RequestBody ServiceDgiDTO serviceDgiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ServiceDgi : {}", serviceDgiDTO);
        if (serviceDgiDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceDgi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceDgiDTO = serviceDgiService.save(serviceDgiDTO);
        return ResponseEntity.created(new URI("/api/service-dgis/" + serviceDgiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceDgiDTO.getId().toString()))
            .body(serviceDgiDTO);
    }

    /**
     * {@code PUT  /service-dgis/:id} : Updates an existing serviceDgi.
     *
     * @param id the id of the serviceDgiDTO to save.
     * @param serviceDgiDTO the serviceDgiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceDgiDTO,
     * or with status {@code 400 (Bad Request)} if the serviceDgiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceDgiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDgiDTO> updateServiceDgi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceDgiDTO serviceDgiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceDgi : {}, {}", id, serviceDgiDTO);
        if (serviceDgiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceDgiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDgiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceDgiDTO = serviceDgiService.update(serviceDgiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceDgiDTO.getId().toString()))
            .body(serviceDgiDTO);
    }

    /**
     * {@code PATCH  /service-dgis/:id} : Partial updates given fields of an existing serviceDgi, field will ignore if it is null
     *
     * @param id the id of the serviceDgiDTO to save.
     * @param serviceDgiDTO the serviceDgiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceDgiDTO,
     * or with status {@code 400 (Bad Request)} if the serviceDgiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceDgiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceDgiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceDgiDTO> partialUpdateServiceDgi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceDgiDTO serviceDgiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceDgi partially : {}, {}", id, serviceDgiDTO);
        if (serviceDgiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceDgiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDgiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceDgiDTO> result = serviceDgiService.partialUpdate(serviceDgiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceDgiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-dgis} : get all the Service Dgis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Service Dgis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceDgiDTO>> getAllServiceDgis(
        ServiceDgiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ServiceDgis by criteria: {}", criteria);

        Page<ServiceDgiDTO> page = serviceDgiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-dgis/count} : count all the serviceDgis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceDgis(ServiceDgiCriteria criteria) {
        LOG.debug("REST request to count ServiceDgis by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceDgiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-dgis/:id} : get the "id" serviceDgi.
     *
     * @param id the id of the serviceDgiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceDgiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDgiDTO> getServiceDgi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceDgi : {}", id);
        Optional<ServiceDgiDTO> serviceDgiDTO = serviceDgiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceDgiDTO);
    }

    /**
     * {@code DELETE  /service-dgis/:id} : delete the "id" serviceDgi.
     *
     * @param id the id of the serviceDgiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceDgi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceDgi : {}", id);
        serviceDgiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
