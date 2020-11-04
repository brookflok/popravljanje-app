package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Poruka;
import com.damir.popravljanje.repository.PorukaRepository;
import com.damir.popravljanje.repository.search.PorukaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Poruka}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PorukaResource {

    private final Logger log = LoggerFactory.getLogger(PorukaResource.class);

    private static final String ENTITY_NAME = "poruka";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PorukaRepository porukaRepository;

    private final PorukaSearchRepository porukaSearchRepository;

    public PorukaResource(PorukaRepository porukaRepository, PorukaSearchRepository porukaSearchRepository) {
        this.porukaRepository = porukaRepository;
        this.porukaSearchRepository = porukaSearchRepository;
    }

    /**
     * {@code POST  /porukas} : Create a new poruka.
     *
     * @param poruka the poruka to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poruka, or with status {@code 400 (Bad Request)} if the poruka has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/porukas")
    public ResponseEntity<Poruka> createPoruka(@RequestBody Poruka poruka) throws URISyntaxException {
        log.debug("REST request to save Poruka : {}", poruka);
        if (poruka.getId() != null) {
            throw new BadRequestAlertException("A new poruka cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Poruka result = porukaRepository.save(poruka);
        porukaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/porukas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /porukas} : Updates an existing poruka.
     *
     * @param poruka the poruka to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poruka,
     * or with status {@code 400 (Bad Request)} if the poruka is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poruka couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/porukas")
    public ResponseEntity<Poruka> updatePoruka(@RequestBody Poruka poruka) throws URISyntaxException {
        log.debug("REST request to update Poruka : {}", poruka);
        if (poruka.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Poruka result = porukaRepository.save(poruka);
        porukaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poruka.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /porukas} : get all the porukas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of porukas in body.
     */
    @GetMapping("/porukas")
    public List<Poruka> getAllPorukas() {
        log.debug("REST request to get all Porukas");
        return porukaRepository.findAll();
    }

    /**
     * {@code GET  /porukas/:id} : get the "id" poruka.
     *
     * @param id the id of the poruka to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poruka, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/porukas/{id}")
    public ResponseEntity<Poruka> getPoruka(@PathVariable Long id) {
        log.debug("REST request to get Poruka : {}", id);
        Optional<Poruka> poruka = porukaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poruka);
    }

    /**
     * {@code DELETE  /porukas/:id} : delete the "id" poruka.
     *
     * @param id the id of the poruka to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/porukas/{id}")
    public ResponseEntity<Void> deletePoruka(@PathVariable Long id) {
        log.debug("REST request to delete Poruka : {}", id);
        porukaRepository.deleteById(id);
        porukaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/porukas?query=:query} : search for the poruka corresponding
     * to the query.
     *
     * @param query the query of the poruka search.
     * @return the result of the search.
     */
    @GetMapping("/_search/porukas")
    public List<Poruka> searchPorukas(@RequestParam String query) {
        log.debug("REST request to search Porukas for query {}", query);
        return StreamSupport
            .stream(porukaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
