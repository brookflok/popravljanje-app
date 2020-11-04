package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.ProfilnaSlika;
import com.damir.popravljanje.repository.ProfilnaSlikaRepository;
import com.damir.popravljanje.repository.search.ProfilnaSlikaSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.ProfilnaSlika}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProfilnaSlikaResource {

    private final Logger log = LoggerFactory.getLogger(ProfilnaSlikaResource.class);

    private static final String ENTITY_NAME = "profilnaSlika";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilnaSlikaRepository profilnaSlikaRepository;

    private final ProfilnaSlikaSearchRepository profilnaSlikaSearchRepository;

    public ProfilnaSlikaResource(ProfilnaSlikaRepository profilnaSlikaRepository, ProfilnaSlikaSearchRepository profilnaSlikaSearchRepository) {
        this.profilnaSlikaRepository = profilnaSlikaRepository;
        this.profilnaSlikaSearchRepository = profilnaSlikaSearchRepository;
    }

    /**
     * {@code POST  /profilna-slikas} : Create a new profilnaSlika.
     *
     * @param profilnaSlika the profilnaSlika to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilnaSlika, or with status {@code 400 (Bad Request)} if the profilnaSlika has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profilna-slikas")
    public ResponseEntity<ProfilnaSlika> createProfilnaSlika(@RequestBody ProfilnaSlika profilnaSlika) throws URISyntaxException {
        log.debug("REST request to save ProfilnaSlika : {}", profilnaSlika);
        if (profilnaSlika.getId() != null) {
            throw new BadRequestAlertException("A new profilnaSlika cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfilnaSlika result = profilnaSlikaRepository.save(profilnaSlika);
        profilnaSlikaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profilna-slikas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profilna-slikas} : Updates an existing profilnaSlika.
     *
     * @param profilnaSlika the profilnaSlika to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilnaSlika,
     * or with status {@code 400 (Bad Request)} if the profilnaSlika is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profilnaSlika couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profilna-slikas")
    public ResponseEntity<ProfilnaSlika> updateProfilnaSlika(@RequestBody ProfilnaSlika profilnaSlika) throws URISyntaxException {
        log.debug("REST request to update ProfilnaSlika : {}", profilnaSlika);
        if (profilnaSlika.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfilnaSlika result = profilnaSlikaRepository.save(profilnaSlika);
        profilnaSlikaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profilnaSlika.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profilna-slikas} : get all the profilnaSlikas.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profilnaSlikas in body.
     */
    @GetMapping("/profilna-slikas")
    public List<ProfilnaSlika> getAllProfilnaSlikas(@RequestParam(required = false) String filter) {
        if ("dodatniinfouser-is-null".equals(filter)) {
            log.debug("REST request to get all ProfilnaSlikas where dodatniInfoUser is null");
            return StreamSupport
                .stream(profilnaSlikaRepository.findAll().spliterator(), false)
                .filter(profilnaSlika -> profilnaSlika.getDodatniInfoUser() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ProfilnaSlikas");
        return profilnaSlikaRepository.findAll();
    }

    /**
     * {@code GET  /profilna-slikas/:id} : get the "id" profilnaSlika.
     *
     * @param id the id of the profilnaSlika to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profilnaSlika, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profilna-slikas/{id}")
    public ResponseEntity<ProfilnaSlika> getProfilnaSlika(@PathVariable Long id) {
        log.debug("REST request to get ProfilnaSlika : {}", id);
        Optional<ProfilnaSlika> profilnaSlika = profilnaSlikaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profilnaSlika);
    }

    /**
     * {@code DELETE  /profilna-slikas/:id} : delete the "id" profilnaSlika.
     *
     * @param id the id of the profilnaSlika to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profilna-slikas/{id}")
    public ResponseEntity<Void> deleteProfilnaSlika(@PathVariable Long id) {
        log.debug("REST request to delete ProfilnaSlika : {}", id);
        profilnaSlikaRepository.deleteById(id);
        profilnaSlikaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/profilna-slikas?query=:query} : search for the profilnaSlika corresponding
     * to the query.
     *
     * @param query the query of the profilnaSlika search.
     * @return the result of the search.
     */
    @GetMapping("/_search/profilna-slikas")
    public List<ProfilnaSlika> searchProfilnaSlikas(@RequestParam String query) {
        log.debug("REST request to search ProfilnaSlikas for query {}", query);
        return StreamSupport
            .stream(profilnaSlikaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
