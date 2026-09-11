package com.dgi.gestionactifs.web.rest;

import com.dgi.gestionactifs.repository.CategorieMaterielRepository;
import com.dgi.gestionactifs.service.CategorieMaterielService;
import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
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
 * REST controller for managing {@link com.dgi.gestionactifs.domain.CategorieMateriel}.
 */
@RestController
@RequestMapping("/api/categorie-materiels")
public class CategorieMaterielResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategorieMaterielResource.class);

    private static final String ENTITY_NAME = "categorieMateriel";

    @Value("${jhipster.clientApp.name:gestionActifsDgi}")
    private String applicationName;

    private final CategorieMaterielService categorieMaterielService;

    private final CategorieMaterielRepository categorieMaterielRepository;

    public CategorieMaterielResource(
        CategorieMaterielService categorieMaterielService,
        CategorieMaterielRepository categorieMaterielRepository
    ) {
        this.categorieMaterielService = categorieMaterielService;
        this.categorieMaterielRepository = categorieMaterielRepository;
    }

    /**
     * {@code POST  /categorie-materiels} : Create a new categorieMateriel.
     *
     * @param categorieMaterielDTO the categorieMaterielDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorieMaterielDTO, or with status {@code 400 (Bad Request)} if the categorieMateriel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategorieMaterielDTO> createCategorieMateriel(@Valid @RequestBody CategorieMaterielDTO categorieMaterielDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategorieMateriel : {}", categorieMaterielDTO);
        if (categorieMaterielDTO.getId() != null) {
            throw new BadRequestAlertException("A new categorieMateriel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categorieMaterielDTO = categorieMaterielService.save(categorieMaterielDTO);
        return ResponseEntity.created(new URI("/api/categorie-materiels/" + categorieMaterielDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categorieMaterielDTO.getId().toString()))
            .body(categorieMaterielDTO);
    }

    /**
     * {@code PUT  /categorie-materiels/:id} : Updates an existing categorieMateriel.
     *
     * @param id the id of the categorieMaterielDTO to save.
     * @param categorieMaterielDTO the categorieMaterielDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieMaterielDTO,
     * or with status {@code 400 (Bad Request)} if the categorieMaterielDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorieMaterielDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategorieMaterielDTO> updateCategorieMateriel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategorieMaterielDTO categorieMaterielDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategorieMateriel : {}, {}", id, categorieMaterielDTO);
        if (categorieMaterielDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieMaterielDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieMaterielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categorieMaterielDTO = categorieMaterielService.update(categorieMaterielDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieMaterielDTO.getId().toString()))
            .body(categorieMaterielDTO);
    }

    /**
     * {@code PATCH  /categorie-materiels/:id} : Partial updates given fields of an existing categorieMateriel, field will ignore if it is null
     *
     * @param id the id of the categorieMaterielDTO to save.
     * @param categorieMaterielDTO the categorieMaterielDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieMaterielDTO,
     * or with status {@code 400 (Bad Request)} if the categorieMaterielDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categorieMaterielDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorieMaterielDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategorieMaterielDTO> partialUpdateCategorieMateriel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategorieMaterielDTO categorieMaterielDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategorieMateriel partially : {}, {}", id, categorieMaterielDTO);
        if (categorieMaterielDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieMaterielDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieMaterielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorieMaterielDTO> result = categorieMaterielService.partialUpdate(categorieMaterielDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieMaterielDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categorie-materiels} : get all the Categorie Materiels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Categorie Materiels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategorieMaterielDTO>> getAllCategorieMateriels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CategorieMateriels");
        Page<CategorieMaterielDTO> page = categorieMaterielService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categorie-materiels/:id} : get the "id" categorieMateriel.
     *
     * @param id the id of the categorieMaterielDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorieMaterielDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategorieMaterielDTO> getCategorieMateriel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategorieMateriel : {}", id);
        Optional<CategorieMaterielDTO> categorieMaterielDTO = categorieMaterielService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorieMaterielDTO);
    }

    /**
     * {@code DELETE  /categorie-materiels/:id} : delete the "id" categorieMateriel.
     *
     * @param id the id of the categorieMaterielDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorieMateriel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategorieMateriel : {}", id);
        categorieMaterielService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
