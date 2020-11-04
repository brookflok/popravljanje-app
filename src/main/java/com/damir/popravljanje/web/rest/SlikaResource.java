package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Slika;
import com.damir.popravljanje.repository.SlikaRepository;
import com.damir.popravljanje.repository.search.SlikaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Slika}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SlikaResource {

    private final Logger log = LoggerFactory.getLogger(SlikaResource.class);

    private static final String ENTITY_NAME = "slika";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlikaRepository slikaRepository;

    private final SlikaSearchRepository slikaSearchRepository;

    public SlikaResource(SlikaRepository slikaRepository, SlikaSearchRepository slikaSearchRepository) {
        this.slikaRepository = slikaRepository;
        this.slikaSearchRepository = slikaSearchRepository;
    }

    /**
     * {@code POST  /slikas} : Create a new slika.
     *
     * @param slika the slika to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slika, or with status {@code 400 (Bad Request)} if the slika has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/slikas")
    public ResponseEntity<Slika> createSlika(@RequestBody Slika slika) throws URISyntaxException {
        log.debug("REST request to save Slika : {}", slika);
        if (slika.getId() != null) {
            throw new BadRequestAlertException("A new slika cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Slika result = slikaRepository.save(slika);
        slikaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/slikas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /slikas} : Updates an existing slika.
     *
     * @param slika the slika to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slika,
     * or with status {@code 400 (Bad Request)} if the slika is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slika couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/slikas")
    public ResponseEntity<Slika> updateSlika(@RequestBody Slika slika) throws URISyntaxException {
        log.debug("REST request to update Slika : {}", slika);
        if (slika.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Slika result = slikaRepository.save(slika);
        slikaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, slika.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /slikas} : get all the slikas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slikas in body.
     */
    @GetMapping("/slikas")
    public List<Slika> getAllSlikas() {
        log.debug("REST request to get all Slikas");
        return slikaRepository.findAll();
    }

    /**
     * {@code GET  /slikas/:id} : get the "id" slika.
     *
     * @param id the id of the slika to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slika, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/slikas/{id}")
    public ResponseEntity<Slika> getSlika(@PathVariable Long id) {
        log.debug("REST request to get Slika : {}", id);
        Optional<Slika> slika = slikaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(slika);
    }

    /**
     * {@code DELETE  /slikas/:id} : delete the "id" slika.
     *
     * @param id the id of the slika to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/slikas/{id}")
    public ResponseEntity<Void> deleteSlika(@PathVariable Long id) {
        log.debug("REST request to delete Slika : {}", id);
        slikaRepository.deleteById(id);
        slikaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/slikas?query=:query} : search for the slika corresponding
     * to the query.
     *
     * @param query the query of the slika search.
     * @return the result of the search.
     */
    @GetMapping("/_search/slikas")
    public List<Slika> searchSlikas(@RequestParam String query) {
        log.debug("REST request to search Slikas for query {}", query);
        return StreamSupport
            .stream(slikaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
