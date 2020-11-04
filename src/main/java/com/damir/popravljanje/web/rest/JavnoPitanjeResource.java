package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.JavnoPitanje;
import com.damir.popravljanje.repository.JavnoPitanjeRepository;
import com.damir.popravljanje.repository.search.JavnoPitanjeSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.JavnoPitanje}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JavnoPitanjeResource {

    private final Logger log = LoggerFactory.getLogger(JavnoPitanjeResource.class);

    private static final String ENTITY_NAME = "javnoPitanje";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JavnoPitanjeRepository javnoPitanjeRepository;

    private final JavnoPitanjeSearchRepository javnoPitanjeSearchRepository;

    public JavnoPitanjeResource(JavnoPitanjeRepository javnoPitanjeRepository, JavnoPitanjeSearchRepository javnoPitanjeSearchRepository) {
        this.javnoPitanjeRepository = javnoPitanjeRepository;
        this.javnoPitanjeSearchRepository = javnoPitanjeSearchRepository;
    }

    /**
     * {@code POST  /javno-pitanjes} : Create a new javnoPitanje.
     *
     * @param javnoPitanje the javnoPitanje to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new javnoPitanje, or with status {@code 400 (Bad Request)} if the javnoPitanje has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/javno-pitanjes")
    public ResponseEntity<JavnoPitanje> createJavnoPitanje(@RequestBody JavnoPitanje javnoPitanje) throws URISyntaxException {
        log.debug("REST request to save JavnoPitanje : {}", javnoPitanje);
        if (javnoPitanje.getId() != null) {
            throw new BadRequestAlertException("A new javnoPitanje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JavnoPitanje result = javnoPitanjeRepository.save(javnoPitanje);
        javnoPitanjeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/javno-pitanjes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /javno-pitanjes} : Updates an existing javnoPitanje.
     *
     * @param javnoPitanje the javnoPitanje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated javnoPitanje,
     * or with status {@code 400 (Bad Request)} if the javnoPitanje is not valid,
     * or with status {@code 500 (Internal Server Error)} if the javnoPitanje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/javno-pitanjes")
    public ResponseEntity<JavnoPitanje> updateJavnoPitanje(@RequestBody JavnoPitanje javnoPitanje) throws URISyntaxException {
        log.debug("REST request to update JavnoPitanje : {}", javnoPitanje);
        if (javnoPitanje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JavnoPitanje result = javnoPitanjeRepository.save(javnoPitanje);
        javnoPitanjeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, javnoPitanje.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /javno-pitanjes} : get all the javnoPitanjes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of javnoPitanjes in body.
     */
    @GetMapping("/javno-pitanjes")
    public List<JavnoPitanje> getAllJavnoPitanjes(@RequestParam(required = false) String filter) {
        if ("odgovornajavnopitanje-is-null".equals(filter)) {
            log.debug("REST request to get all JavnoPitanjes where odgovorNaJavnoPitanje is null");
            return StreamSupport
                .stream(javnoPitanjeRepository.findAll().spliterator(), false)
                .filter(javnoPitanje -> javnoPitanje.getOdgovorNaJavnoPitanje() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all JavnoPitanjes");
        return javnoPitanjeRepository.findAll();
    }

    /**
     * {@code GET  /javno-pitanjes/:id} : get the "id" javnoPitanje.
     *
     * @param id the id of the javnoPitanje to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the javnoPitanje, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/javno-pitanjes/{id}")
    public ResponseEntity<JavnoPitanje> getJavnoPitanje(@PathVariable Long id) {
        log.debug("REST request to get JavnoPitanje : {}", id);
        Optional<JavnoPitanje> javnoPitanje = javnoPitanjeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(javnoPitanje);
    }

    /**
     * {@code DELETE  /javno-pitanjes/:id} : delete the "id" javnoPitanje.
     *
     * @param id the id of the javnoPitanje to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/javno-pitanjes/{id}")
    public ResponseEntity<Void> deleteJavnoPitanje(@PathVariable Long id) {
        log.debug("REST request to delete JavnoPitanje : {}", id);
        javnoPitanjeRepository.deleteById(id);
        javnoPitanjeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/javno-pitanjes?query=:query} : search for the javnoPitanje corresponding
     * to the query.
     *
     * @param query the query of the javnoPitanje search.
     * @return the result of the search.
     */
    @GetMapping("/_search/javno-pitanjes")
    public List<JavnoPitanje> searchJavnoPitanjes(@RequestParam String query) {
        log.debug("REST request to search JavnoPitanjes for query {}", query);
        return StreamSupport
            .stream(javnoPitanjeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
