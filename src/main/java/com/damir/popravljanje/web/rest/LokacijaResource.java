package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Lokacija;
import com.damir.popravljanje.repository.LokacijaRepository;
import com.damir.popravljanje.repository.search.LokacijaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Lokacija}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LokacijaResource {

    private final Logger log = LoggerFactory.getLogger(LokacijaResource.class);

    private static final String ENTITY_NAME = "lokacija";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LokacijaRepository lokacijaRepository;

    private final LokacijaSearchRepository lokacijaSearchRepository;

    public LokacijaResource(LokacijaRepository lokacijaRepository, LokacijaSearchRepository lokacijaSearchRepository) {
        this.lokacijaRepository = lokacijaRepository;
        this.lokacijaSearchRepository = lokacijaSearchRepository;
    }

    /**
     * {@code POST  /lokacijas} : Create a new lokacija.
     *
     * @param lokacija the lokacija to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lokacija, or with status {@code 400 (Bad Request)} if the lokacija has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lokacijas")
    public ResponseEntity<Lokacija> createLokacija(@RequestBody Lokacija lokacija) throws URISyntaxException {
        log.debug("REST request to save Lokacija : {}", lokacija);
        if (lokacija.getId() != null) {
            throw new BadRequestAlertException("A new lokacija cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lokacija result = lokacijaRepository.save(lokacija);
        lokacijaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lokacijas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lokacijas} : Updates an existing lokacija.
     *
     * @param lokacija the lokacija to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lokacija,
     * or with status {@code 400 (Bad Request)} if the lokacija is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lokacija couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lokacijas")
    public ResponseEntity<Lokacija> updateLokacija(@RequestBody Lokacija lokacija) throws URISyntaxException {
        log.debug("REST request to update Lokacija : {}", lokacija);
        if (lokacija.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Lokacija result = lokacijaRepository.save(lokacija);
        lokacijaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lokacija.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lokacijas} : get all the lokacijas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lokacijas in body.
     */
    @GetMapping("/lokacijas")
    public List<Lokacija> getAllLokacijas() {
        log.debug("REST request to get all Lokacijas");
        return lokacijaRepository.findAll();
    }

    /**
     * {@code GET  /lokacijas/:id} : get the "id" lokacija.
     *
     * @param id the id of the lokacija to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lokacija, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lokacijas/{id}")
    public ResponseEntity<Lokacija> getLokacija(@PathVariable Long id) {
        log.debug("REST request to get Lokacija : {}", id);
        Optional<Lokacija> lokacija = lokacijaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lokacija);
    }

    /**
     * {@code DELETE  /lokacijas/:id} : delete the "id" lokacija.
     *
     * @param id the id of the lokacija to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lokacijas/{id}")
    public ResponseEntity<Void> deleteLokacija(@PathVariable Long id) {
        log.debug("REST request to delete Lokacija : {}", id);
        lokacijaRepository.deleteById(id);
        lokacijaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/lokacijas?query=:query} : search for the lokacija corresponding
     * to the query.
     *
     * @param query the query of the lokacija search.
     * @return the result of the search.
     */
    @GetMapping("/_search/lokacijas")
    public List<Lokacija> searchLokacijas(@RequestParam String query) {
        log.debug("REST request to search Lokacijas for query {}", query);
        return StreamSupport
            .stream(lokacijaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
