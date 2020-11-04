package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Usluga;
import com.damir.popravljanje.repository.UslugaRepository;
import com.damir.popravljanje.repository.search.UslugaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Usluga}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UslugaResource {

    private final Logger log = LoggerFactory.getLogger(UslugaResource.class);

    private static final String ENTITY_NAME = "usluga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UslugaRepository uslugaRepository;

    private final UslugaSearchRepository uslugaSearchRepository;

    public UslugaResource(UslugaRepository uslugaRepository, UslugaSearchRepository uslugaSearchRepository) {
        this.uslugaRepository = uslugaRepository;
        this.uslugaSearchRepository = uslugaSearchRepository;
    }

    /**
     * {@code POST  /uslugas} : Create a new usluga.
     *
     * @param usluga the usluga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usluga, or with status {@code 400 (Bad Request)} if the usluga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uslugas")
    public ResponseEntity<Usluga> createUsluga(@RequestBody Usluga usluga) throws URISyntaxException {
        log.debug("REST request to save Usluga : {}", usluga);
        if (usluga.getId() != null) {
            throw new BadRequestAlertException("A new usluga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Usluga result = uslugaRepository.save(usluga);
        uslugaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/uslugas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uslugas} : Updates an existing usluga.
     *
     * @param usluga the usluga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usluga,
     * or with status {@code 400 (Bad Request)} if the usluga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usluga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uslugas")
    public ResponseEntity<Usluga> updateUsluga(@RequestBody Usluga usluga) throws URISyntaxException {
        log.debug("REST request to update Usluga : {}", usluga);
        if (usluga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Usluga result = uslugaRepository.save(usluga);
        uslugaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usluga.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /uslugas} : get all the uslugas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uslugas in body.
     */
    @GetMapping("/uslugas")
    public List<Usluga> getAllUslugas() {
        log.debug("REST request to get all Uslugas");
        return uslugaRepository.findAll();
    }

    /**
     * {@code GET  /uslugas/:id} : get the "id" usluga.
     *
     * @param id the id of the usluga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usluga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uslugas/{id}")
    public ResponseEntity<Usluga> getUsluga(@PathVariable Long id) {
        log.debug("REST request to get Usluga : {}", id);
        Optional<Usluga> usluga = uslugaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usluga);
    }

    /**
     * {@code DELETE  /uslugas/:id} : delete the "id" usluga.
     *
     * @param id the id of the usluga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uslugas/{id}")
    public ResponseEntity<Void> deleteUsluga(@PathVariable Long id) {
        log.debug("REST request to delete Usluga : {}", id);
        uslugaRepository.deleteById(id);
        uslugaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/uslugas?query=:query} : search for the usluga corresponding
     * to the query.
     *
     * @param query the query of the usluga search.
     * @return the result of the search.
     */
    @GetMapping("/_search/uslugas")
    public List<Usluga> searchUslugas(@RequestParam String query) {
        log.debug("REST request to search Uslugas for query {}", query);
        return StreamSupport
            .stream(uslugaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
