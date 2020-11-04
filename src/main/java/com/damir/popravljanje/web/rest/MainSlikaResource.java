package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.MainSlika;
import com.damir.popravljanje.repository.MainSlikaRepository;
import com.damir.popravljanje.repository.search.MainSlikaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.MainSlika}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MainSlikaResource {

    private final Logger log = LoggerFactory.getLogger(MainSlikaResource.class);

    private static final String ENTITY_NAME = "mainSlika";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MainSlikaRepository mainSlikaRepository;

    private final MainSlikaSearchRepository mainSlikaSearchRepository;

    public MainSlikaResource(MainSlikaRepository mainSlikaRepository, MainSlikaSearchRepository mainSlikaSearchRepository) {
        this.mainSlikaRepository = mainSlikaRepository;
        this.mainSlikaSearchRepository = mainSlikaSearchRepository;
    }

    /**
     * {@code POST  /main-slikas} : Create a new mainSlika.
     *
     * @param mainSlika the mainSlika to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mainSlika, or with status {@code 400 (Bad Request)} if the mainSlika has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/main-slikas")
    public ResponseEntity<MainSlika> createMainSlika(@RequestBody MainSlika mainSlika) throws URISyntaxException {
        log.debug("REST request to save MainSlika : {}", mainSlika);
        if (mainSlika.getId() != null) {
            throw new BadRequestAlertException("A new mainSlika cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MainSlika result = mainSlikaRepository.save(mainSlika);
        mainSlikaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/main-slikas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /main-slikas} : Updates an existing mainSlika.
     *
     * @param mainSlika the mainSlika to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mainSlika,
     * or with status {@code 400 (Bad Request)} if the mainSlika is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mainSlika couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/main-slikas")
    public ResponseEntity<MainSlika> updateMainSlika(@RequestBody MainSlika mainSlika) throws URISyntaxException {
        log.debug("REST request to update MainSlika : {}", mainSlika);
        if (mainSlika.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MainSlika result = mainSlikaRepository.save(mainSlika);
        mainSlikaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mainSlika.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /main-slikas} : get all the mainSlikas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mainSlikas in body.
     */
    @GetMapping("/main-slikas")
    public List<MainSlika> getAllMainSlikas() {
        log.debug("REST request to get all MainSlikas");
        return mainSlikaRepository.findAll();
    }

    /**
     * {@code GET  /main-slikas/:id} : get the "id" mainSlika.
     *
     * @param id the id of the mainSlika to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mainSlika, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/main-slikas/{id}")
    public ResponseEntity<MainSlika> getMainSlika(@PathVariable Long id) {
        log.debug("REST request to get MainSlika : {}", id);
        Optional<MainSlika> mainSlika = mainSlikaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mainSlika);
    }

    /**
     * {@code DELETE  /main-slikas/:id} : delete the "id" mainSlika.
     *
     * @param id the id of the mainSlika to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/main-slikas/{id}")
    public ResponseEntity<Void> deleteMainSlika(@PathVariable Long id) {
        log.debug("REST request to delete MainSlika : {}", id);
        mainSlikaRepository.deleteById(id);
        mainSlikaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/main-slikas?query=:query} : search for the mainSlika corresponding
     * to the query.
     *
     * @param query the query of the mainSlika search.
     * @return the result of the search.
     */
    @GetMapping("/_search/main-slikas")
    public List<MainSlika> searchMainSlikas(@RequestParam String query) {
        log.debug("REST request to search MainSlikas for query {}", query);
        return StreamSupport
            .stream(mainSlikaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
