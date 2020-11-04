package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Entiteti;
import com.damir.popravljanje.repository.EntitetiRepository;
import com.damir.popravljanje.repository.search.EntitetiSearchRepository;
import com.damir.popravljanje.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.damir.popravljanje.domain.Entiteti}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntitetiResource {

    private final Logger log = LoggerFactory.getLogger(EntitetiResource.class);

    private static final String ENTITY_NAME = "entiteti";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntitetiRepository entitetiRepository;

    private final EntitetiSearchRepository entitetiSearchRepository;

    public EntitetiResource(EntitetiRepository entitetiRepository, EntitetiSearchRepository entitetiSearchRepository) {
        this.entitetiRepository = entitetiRepository;
        this.entitetiSearchRepository = entitetiSearchRepository;
    }

    /**
     * {@code POST  /entitetis} : Create a new entiteti.
     *
     * @param entiteti the entiteti to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entiteti, or with status {@code 400 (Bad Request)} if the entiteti has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entitetis")
    public ResponseEntity<Entiteti> createEntiteti(@RequestBody Entiteti entiteti) throws URISyntaxException {
        log.debug("REST request to save Entiteti : {}", entiteti);
        if (entiteti.getId() != null) {
            throw new BadRequestAlertException("A new entiteti cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entiteti result = entitetiRepository.save(entiteti);
        entitetiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/entitetis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entitetis} : Updates an existing entiteti.
     *
     * @param entiteti the entiteti to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entiteti,
     * or with status {@code 400 (Bad Request)} if the entiteti is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entiteti couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entitetis")
    public ResponseEntity<Entiteti> updateEntiteti(@RequestBody Entiteti entiteti) throws URISyntaxException {
        log.debug("REST request to update Entiteti : {}", entiteti);
        if (entiteti.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entiteti result = entitetiRepository.save(entiteti);
        entitetiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entiteti.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entitetis} : get all the entitetis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entitetis in body.
     */
    @GetMapping("/entitetis")
    public List<Entiteti> getAllEntitetis() {
        log.debug("REST request to get all Entitetis");
        return entitetiRepository.findAll();
    }

    /**
     * {@code GET  /entitetis/:id} : get the "id" entiteti.
     *
     * @param id the id of the entiteti to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entiteti, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entitetis/{id}")
    public ResponseEntity<Entiteti> getEntiteti(@PathVariable Long id) {
        log.debug("REST request to get Entiteti : {}", id);
        Optional<Entiteti> entiteti = entitetiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entiteti);
    }

    /**
     * {@code DELETE  /entitetis/:id} : delete the "id" entiteti.
     *
     * @param id the id of the entiteti to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entitetis/{id}")
    public ResponseEntity<Void> deleteEntiteti(@PathVariable Long id) {
        log.debug("REST request to delete Entiteti : {}", id);
        entitetiRepository.deleteById(id);
        entitetiSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/entitetis?query=:query} : search for the entiteti corresponding
     * to the query.
     *
     * @param query the query of the entiteti search.
     * @return the result of the search.
     */
    @GetMapping("/_search/entitetis")
    public List<Entiteti> searchEntitetis(@RequestParam String query) {
        log.debug("REST request to search Entitetis for query {}", query);
        return StreamSupport
            .stream(entitetiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
