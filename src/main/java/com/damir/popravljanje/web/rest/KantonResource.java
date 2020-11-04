package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Kanton;
import com.damir.popravljanje.repository.KantonRepository;
import com.damir.popravljanje.repository.search.KantonSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Kanton}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KantonResource {

    private final Logger log = LoggerFactory.getLogger(KantonResource.class);

    private static final String ENTITY_NAME = "kanton";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KantonRepository kantonRepository;

    private final KantonSearchRepository kantonSearchRepository;

    public KantonResource(KantonRepository kantonRepository, KantonSearchRepository kantonSearchRepository) {
        this.kantonRepository = kantonRepository;
        this.kantonSearchRepository = kantonSearchRepository;
    }

    /**
     * {@code POST  /kantons} : Create a new kanton.
     *
     * @param kanton the kanton to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kanton, or with status {@code 400 (Bad Request)} if the kanton has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kantons")
    public ResponseEntity<Kanton> createKanton(@RequestBody Kanton kanton) throws URISyntaxException {
        log.debug("REST request to save Kanton : {}", kanton);
        if (kanton.getId() != null) {
            throw new BadRequestAlertException("A new kanton cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kanton result = kantonRepository.save(kanton);
        kantonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/kantons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kantons} : Updates an existing kanton.
     *
     * @param kanton the kanton to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kanton,
     * or with status {@code 400 (Bad Request)} if the kanton is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kanton couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kantons")
    public ResponseEntity<Kanton> updateKanton(@RequestBody Kanton kanton) throws URISyntaxException {
        log.debug("REST request to update Kanton : {}", kanton);
        if (kanton.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Kanton result = kantonRepository.save(kanton);
        kantonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kanton.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kantons} : get all the kantons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kantons in body.
     */
    @GetMapping("/kantons")
    public List<Kanton> getAllKantons() {
        log.debug("REST request to get all Kantons");
        return kantonRepository.findAll();
    }

    /**
     * {@code GET  /kantons/:id} : get the "id" kanton.
     *
     * @param id the id of the kanton to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kanton, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kantons/{id}")
    public ResponseEntity<Kanton> getKanton(@PathVariable Long id) {
        log.debug("REST request to get Kanton : {}", id);
        Optional<Kanton> kanton = kantonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kanton);
    }

    /**
     * {@code DELETE  /kantons/:id} : delete the "id" kanton.
     *
     * @param id the id of the kanton to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kantons/{id}")
    public ResponseEntity<Void> deleteKanton(@PathVariable Long id) {
        log.debug("REST request to delete Kanton : {}", id);
        kantonRepository.deleteById(id);
        kantonSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/kantons?query=:query} : search for the kanton corresponding
     * to the query.
     *
     * @param query the query of the kanton search.
     * @return the result of the search.
     */
    @GetMapping("/_search/kantons")
    public List<Kanton> searchKantons(@RequestParam String query) {
        log.debug("REST request to search Kantons for query {}", query);
        return StreamSupport
            .stream(kantonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
