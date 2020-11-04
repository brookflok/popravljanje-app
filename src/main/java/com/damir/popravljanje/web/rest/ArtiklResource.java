package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.Artikl;
import com.damir.popravljanje.repository.ArtiklRepository;
import com.damir.popravljanje.repository.search.ArtiklSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.Artikl}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArtiklResource {

    private final Logger log = LoggerFactory.getLogger(ArtiklResource.class);

    private static final String ENTITY_NAME = "artikl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtiklRepository artiklRepository;

    private final ArtiklSearchRepository artiklSearchRepository;

    public ArtiklResource(ArtiklRepository artiklRepository, ArtiklSearchRepository artiklSearchRepository) {
        this.artiklRepository = artiklRepository;
        this.artiklSearchRepository = artiklSearchRepository;
    }

    /**
     * {@code POST  /artikls} : Create a new artikl.
     *
     * @param artikl the artikl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artikl, or with status {@code 400 (Bad Request)} if the artikl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artikls")
    public ResponseEntity<Artikl> createArtikl(@RequestBody Artikl artikl) throws URISyntaxException {
        log.debug("REST request to save Artikl : {}", artikl);
        if (artikl.getId() != null) {
            throw new BadRequestAlertException("A new artikl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Artikl result = artiklRepository.save(artikl);
        artiklSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/artikls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artikls} : Updates an existing artikl.
     *
     * @param artikl the artikl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artikl,
     * or with status {@code 400 (Bad Request)} if the artikl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artikl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artikls")
    public ResponseEntity<Artikl> updateArtikl(@RequestBody Artikl artikl) throws URISyntaxException {
        log.debug("REST request to update Artikl : {}", artikl);
        if (artikl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Artikl result = artiklRepository.save(artikl);
        artiklSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artikl.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artikls} : get all the artikls.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artikls in body.
     */
    @GetMapping("/artikls")
    public List<Artikl> getAllArtikls(@RequestParam(required = false) String filter) {
        if ("informacije-is-null".equals(filter)) {
            log.debug("REST request to get all Artikls where informacije is null");
            return StreamSupport
                .stream(artiklRepository.findAll().spliterator(), false)
                .filter(artikl -> artikl.getInformacije() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Artikls");
        return artiklRepository.findAll();
    }

    /**
     * {@code GET  /artikls/:id} : get the "id" artikl.
     *
     * @param id the id of the artikl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artikl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artikls/{id}")
    public ResponseEntity<Artikl> getArtikl(@PathVariable Long id) {
        log.debug("REST request to get Artikl : {}", id);
        Optional<Artikl> artikl = artiklRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(artikl);
    }

    /**
     * {@code DELETE  /artikls/:id} : delete the "id" artikl.
     *
     * @param id the id of the artikl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artikls/{id}")
    public ResponseEntity<Void> deleteArtikl(@PathVariable Long id) {
        log.debug("REST request to delete Artikl : {}", id);
        artiklRepository.deleteById(id);
        artiklSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/artikls?query=:query} : search for the artikl corresponding
     * to the query.
     *
     * @param query the query of the artikl search.
     * @return the result of the search.
     */
    @GetMapping("/_search/artikls")
    public List<Artikl> searchArtikls(@RequestParam String query) {
        log.debug("REST request to search Artikls for query {}", query);
        return StreamSupport
            .stream(artiklSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
