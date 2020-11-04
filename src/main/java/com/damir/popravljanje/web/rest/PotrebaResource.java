package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Potreba;
import com.damir.popravljanje.repository.PotrebaRepository;
import com.damir.popravljanje.repository.search.PotrebaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Potreba}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PotrebaResource {

    private final Logger log = LoggerFactory.getLogger(PotrebaResource.class);

    private static final String ENTITY_NAME = "potreba";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PotrebaRepository potrebaRepository;

    private final PotrebaSearchRepository potrebaSearchRepository;

    public PotrebaResource(PotrebaRepository potrebaRepository, PotrebaSearchRepository potrebaSearchRepository) {
        this.potrebaRepository = potrebaRepository;
        this.potrebaSearchRepository = potrebaSearchRepository;
    }

    /**
     * {@code POST  /potrebas} : Create a new potreba.
     *
     * @param potreba the potreba to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new potreba, or with status {@code 400 (Bad Request)} if the potreba has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/potrebas")
    public ResponseEntity<Potreba> createPotreba(@RequestBody Potreba potreba) throws URISyntaxException {
        log.debug("REST request to save Potreba : {}", potreba);
        if (potreba.getId() != null) {
            throw new BadRequestAlertException("A new potreba cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Potreba result = potrebaRepository.save(potreba);
        potrebaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/potrebas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /potrebas} : Updates an existing potreba.
     *
     * @param potreba the potreba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potreba,
     * or with status {@code 400 (Bad Request)} if the potreba is not valid,
     * or with status {@code 500 (Internal Server Error)} if the potreba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/potrebas")
    public ResponseEntity<Potreba> updatePotreba(@RequestBody Potreba potreba) throws URISyntaxException {
        log.debug("REST request to update Potreba : {}", potreba);
        if (potreba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Potreba result = potrebaRepository.save(potreba);
        potrebaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, potreba.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /potrebas} : get all the potrebas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of potrebas in body.
     */
    @GetMapping("/potrebas")
    public List<Potreba> getAllPotrebas() {
        log.debug("REST request to get all Potrebas");
        return potrebaRepository.findAll();
    }

    /**
     * {@code GET  /potrebas/:id} : get the "id" potreba.
     *
     * @param id the id of the potreba to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the potreba, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/potrebas/{id}")
    public ResponseEntity<Potreba> getPotreba(@PathVariable Long id) {
        log.debug("REST request to get Potreba : {}", id);
        Optional<Potreba> potreba = potrebaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(potreba);
    }

    /**
     * {@code DELETE  /potrebas/:id} : delete the "id" potreba.
     *
     * @param id the id of the potreba to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/potrebas/{id}")
    public ResponseEntity<Void> deletePotreba(@PathVariable Long id) {
        log.debug("REST request to delete Potreba : {}", id);
        potrebaRepository.deleteById(id);
        potrebaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/potrebas?query=:query} : search for the potreba corresponding
     * to the query.
     *
     * @param query the query of the potreba search.
     * @return the result of the search.
     */
    @GetMapping("/_search/potrebas")
    public List<Potreba> searchPotrebas(@RequestParam String query) {
        log.debug("REST request to search Potrebas for query {}", query);
        return StreamSupport
            .stream(potrebaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
