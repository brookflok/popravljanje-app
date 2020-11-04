package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Ucesnici;
import com.damir.popravljanje.repository.UcesniciRepository;
import com.damir.popravljanje.repository.search.UcesniciSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Ucesnici}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UcesniciResource {

    private final Logger log = LoggerFactory.getLogger(UcesniciResource.class);

    private static final String ENTITY_NAME = "ucesnici";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UcesniciRepository ucesniciRepository;

    private final UcesniciSearchRepository ucesniciSearchRepository;

    public UcesniciResource(UcesniciRepository ucesniciRepository, UcesniciSearchRepository ucesniciSearchRepository) {
        this.ucesniciRepository = ucesniciRepository;
        this.ucesniciSearchRepository = ucesniciSearchRepository;
    }

    /**
     * {@code POST  /ucesnicis} : Create a new ucesnici.
     *
     * @param ucesnici the ucesnici to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ucesnici, or with status {@code 400 (Bad Request)} if the ucesnici has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ucesnicis")
    public ResponseEntity<Ucesnici> createUcesnici(@RequestBody Ucesnici ucesnici) throws URISyntaxException {
        log.debug("REST request to save Ucesnici : {}", ucesnici);
        if (ucesnici.getId() != null) {
            throw new BadRequestAlertException("A new ucesnici cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ucesnici result = ucesniciRepository.save(ucesnici);
        ucesniciSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ucesnicis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ucesnicis} : Updates an existing ucesnici.
     *
     * @param ucesnici the ucesnici to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ucesnici,
     * or with status {@code 400 (Bad Request)} if the ucesnici is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ucesnici couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ucesnicis")
    public ResponseEntity<Ucesnici> updateUcesnici(@RequestBody Ucesnici ucesnici) throws URISyntaxException {
        log.debug("REST request to update Ucesnici : {}", ucesnici);
        if (ucesnici.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ucesnici result = ucesniciRepository.save(ucesnici);
        ucesniciSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ucesnici.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ucesnicis} : get all the ucesnicis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ucesnicis in body.
     */
    @GetMapping("/ucesnicis")
    public List<Ucesnici> getAllUcesnicis() {
        log.debug("REST request to get all Ucesnicis");
        return ucesniciRepository.findAll();
    }

    /**
     * {@code GET  /ucesnicis/:id} : get the "id" ucesnici.
     *
     * @param id the id of the ucesnici to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ucesnici, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ucesnicis/{id}")
    public ResponseEntity<Ucesnici> getUcesnici(@PathVariable Long id) {
        log.debug("REST request to get Ucesnici : {}", id);
        Optional<Ucesnici> ucesnici = ucesniciRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ucesnici);
    }

    /**
     * {@code DELETE  /ucesnicis/:id} : delete the "id" ucesnici.
     *
     * @param id the id of the ucesnici to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ucesnicis/{id}")
    public ResponseEntity<Void> deleteUcesnici(@PathVariable Long id) {
        log.debug("REST request to delete Ucesnici : {}", id);
        ucesniciRepository.deleteById(id);
        ucesniciSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ucesnicis?query=:query} : search for the ucesnici corresponding
     * to the query.
     *
     * @param query the query of the ucesnici search.
     * @return the result of the search.
     */
    @GetMapping("/_search/ucesnicis")
    public List<Ucesnici> searchUcesnicis(@RequestParam String query) {
        log.debug("REST request to search Ucesnicis for query {}", query);
        return StreamSupport
            .stream(ucesniciSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
