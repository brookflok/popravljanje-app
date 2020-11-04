package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Galerija;
import com.damir.popravljanje.repository.GalerijaRepository;
import com.damir.popravljanje.repository.search.GalerijaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Galerija}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GalerijaResource {

    private final Logger log = LoggerFactory.getLogger(GalerijaResource.class);

    private static final String ENTITY_NAME = "galerija";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GalerijaRepository galerijaRepository;

    private final GalerijaSearchRepository galerijaSearchRepository;

    public GalerijaResource(GalerijaRepository galerijaRepository, GalerijaSearchRepository galerijaSearchRepository) {
        this.galerijaRepository = galerijaRepository;
        this.galerijaSearchRepository = galerijaSearchRepository;
    }

    /**
     * {@code POST  /galerijas} : Create a new galerija.
     *
     * @param galerija the galerija to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new galerija, or with status {@code 400 (Bad Request)} if the galerija has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/galerijas")
    public ResponseEntity<Galerija> createGalerija(@RequestBody Galerija galerija) throws URISyntaxException {
        log.debug("REST request to save Galerija : {}", galerija);
        if (galerija.getId() != null) {
            throw new BadRequestAlertException("A new galerija cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Galerija result = galerijaRepository.save(galerija);
        galerijaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/galerijas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /galerijas} : Updates an existing galerija.
     *
     * @param galerija the galerija to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galerija,
     * or with status {@code 400 (Bad Request)} if the galerija is not valid,
     * or with status {@code 500 (Internal Server Error)} if the galerija couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/galerijas")
    public ResponseEntity<Galerija> updateGalerija(@RequestBody Galerija galerija) throws URISyntaxException {
        log.debug("REST request to update Galerija : {}", galerija);
        if (galerija.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Galerija result = galerijaRepository.save(galerija);
        galerijaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galerija.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /galerijas} : get all the galerijas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of galerijas in body.
     */
    @GetMapping("/galerijas")
    public List<Galerija> getAllGalerijas() {
        log.debug("REST request to get all Galerijas");
        return galerijaRepository.findAll();
    }

    /**
     * {@code GET  /galerijas/:id} : get the "id" galerija.
     *
     * @param id the id of the galerija to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the galerija, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/galerijas/{id}")
    public ResponseEntity<Galerija> getGalerija(@PathVariable Long id) {
        log.debug("REST request to get Galerija : {}", id);
        Optional<Galerija> galerija = galerijaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(galerija);
    }

    /**
     * {@code DELETE  /galerijas/:id} : delete the "id" galerija.
     *
     * @param id the id of the galerija to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/galerijas/{id}")
    public ResponseEntity<Void> deleteGalerija(@PathVariable Long id) {
        log.debug("REST request to delete Galerija : {}", id);
        galerijaRepository.deleteById(id);
        galerijaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/galerijas?query=:query} : search for the galerija corresponding
     * to the query.
     *
     * @param query the query of the galerija search.
     * @return the result of the search.
     */
    @GetMapping("/_search/galerijas")
    public List<Galerija> searchGalerijas(@RequestParam String query) {
        log.debug("REST request to search Galerijas for query {}", query);
        return StreamSupport
            .stream(galerijaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
