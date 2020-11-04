package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Informacije;
import com.damir.popravljanje.repository.InformacijeRepository;
import com.damir.popravljanje.repository.search.InformacijeSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Informacije}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InformacijeResource {

    private final Logger log = LoggerFactory.getLogger(InformacijeResource.class);

    private static final String ENTITY_NAME = "informacije";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformacijeRepository informacijeRepository;

    private final InformacijeSearchRepository informacijeSearchRepository;

    public InformacijeResource(InformacijeRepository informacijeRepository, InformacijeSearchRepository informacijeSearchRepository) {
        this.informacijeRepository = informacijeRepository;
        this.informacijeSearchRepository = informacijeSearchRepository;
    }

    /**
     * {@code POST  /informacijes} : Create a new informacije.
     *
     * @param informacije the informacije to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informacije, or with status {@code 400 (Bad Request)} if the informacije has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/informacijes")
    public ResponseEntity<Informacije> createInformacije(@RequestBody Informacije informacije) throws URISyntaxException {
        log.debug("REST request to save Informacije : {}", informacije);
        if (informacije.getId() != null) {
            throw new BadRequestAlertException("A new informacije cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Informacije result = informacijeRepository.save(informacije);
        informacijeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/informacijes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /informacijes} : Updates an existing informacije.
     *
     * @param informacije the informacije to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informacije,
     * or with status {@code 400 (Bad Request)} if the informacije is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informacije couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/informacijes")
    public ResponseEntity<Informacije> updateInformacije(@RequestBody Informacije informacije) throws URISyntaxException {
        log.debug("REST request to update Informacije : {}", informacije);
        if (informacije.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Informacije result = informacijeRepository.save(informacije);
        informacijeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informacije.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /informacijes} : get all the informacijes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informacijes in body.
     */
    @GetMapping("/informacijes")
    public List<Informacije> getAllInformacijes() {
        log.debug("REST request to get all Informacijes");
        return informacijeRepository.findAll();
    }

    /**
     * {@code GET  /informacijes/:id} : get the "id" informacije.
     *
     * @param id the id of the informacije to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informacije, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/informacijes/{id}")
    public ResponseEntity<Informacije> getInformacije(@PathVariable Long id) {
        log.debug("REST request to get Informacije : {}", id);
        Optional<Informacije> informacije = informacijeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(informacije);
    }

    /**
     * {@code DELETE  /informacijes/:id} : delete the "id" informacije.
     *
     * @param id the id of the informacije to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/informacijes/{id}")
    public ResponseEntity<Void> deleteInformacije(@PathVariable Long id) {
        log.debug("REST request to delete Informacije : {}", id);
        informacijeRepository.deleteById(id);
        informacijeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/informacijes?query=:query} : search for the informacije corresponding
     * to the query.
     *
     * @param query the query of the informacije search.
     * @return the result of the search.
     */
    @GetMapping("/_search/informacijes")
    public List<Informacije> searchInformacijes(@RequestParam String query) {
        log.debug("REST request to search Informacijes for query {}", query);
        return StreamSupport
            .stream(informacijeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
