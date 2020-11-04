package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.domain.DodatniInfoUser;
import com.damir.popravljanje.repository.DodatniInfoUserRepository;
import com.damir.popravljanje.repository.search.DodatniInfoUserSearchRepository;
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
 * REST controller for managing {@link com.damir.popravljanje.domain.DodatniInfoUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DodatniInfoUserResource {

    private final Logger log = LoggerFactory.getLogger(DodatniInfoUserResource.class);

    private static final String ENTITY_NAME = "dodatniInfoUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DodatniInfoUserRepository dodatniInfoUserRepository;

    private final DodatniInfoUserSearchRepository dodatniInfoUserSearchRepository;

    public DodatniInfoUserResource(DodatniInfoUserRepository dodatniInfoUserRepository, DodatniInfoUserSearchRepository dodatniInfoUserSearchRepository) {
        this.dodatniInfoUserRepository = dodatniInfoUserRepository;
        this.dodatniInfoUserSearchRepository = dodatniInfoUserSearchRepository;
    }

    /**
     * {@code POST  /dodatni-info-users} : Create a new dodatniInfoUser.
     *
     * @param dodatniInfoUser the dodatniInfoUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dodatniInfoUser, or with status {@code 400 (Bad Request)} if the dodatniInfoUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dodatni-info-users")
    public ResponseEntity<DodatniInfoUser> createDodatniInfoUser(@RequestBody DodatniInfoUser dodatniInfoUser) throws URISyntaxException {
        log.debug("REST request to save DodatniInfoUser : {}", dodatniInfoUser);
        if (dodatniInfoUser.getId() != null) {
            throw new BadRequestAlertException("A new dodatniInfoUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DodatniInfoUser result = dodatniInfoUserRepository.save(dodatniInfoUser);
        dodatniInfoUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dodatni-info-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dodatni-info-users} : Updates an existing dodatniInfoUser.
     *
     * @param dodatniInfoUser the dodatniInfoUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dodatniInfoUser,
     * or with status {@code 400 (Bad Request)} if the dodatniInfoUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dodatniInfoUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dodatni-info-users")
    public ResponseEntity<DodatniInfoUser> updateDodatniInfoUser(@RequestBody DodatniInfoUser dodatniInfoUser) throws URISyntaxException {
        log.debug("REST request to update DodatniInfoUser : {}", dodatniInfoUser);
        if (dodatniInfoUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DodatniInfoUser result = dodatniInfoUserRepository.save(dodatniInfoUser);
        dodatniInfoUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dodatniInfoUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dodatni-info-users} : get all the dodatniInfoUsers.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dodatniInfoUsers in body.
     */
    @GetMapping("/dodatni-info-users")
    public List<DodatniInfoUser> getAllDodatniInfoUsers(@RequestParam(required = false) String filter) {
        if ("poruka-is-null".equals(filter)) {
            log.debug("REST request to get all DodatniInfoUsers where poruka is null");
            return StreamSupport
                .stream(dodatniInfoUserRepository.findAll().spliterator(), false)
                .filter(dodatniInfoUser -> dodatniInfoUser.getPoruka() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all DodatniInfoUsers");
        return dodatniInfoUserRepository.findAll();
    }

    /**
     * {@code GET  /dodatni-info-users/:id} : get the "id" dodatniInfoUser.
     *
     * @param id the id of the dodatniInfoUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dodatniInfoUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dodatni-info-users/{id}")
    public ResponseEntity<DodatniInfoUser> getDodatniInfoUser(@PathVariable Long id) {
        log.debug("REST request to get DodatniInfoUser : {}", id);
        Optional<DodatniInfoUser> dodatniInfoUser = dodatniInfoUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dodatniInfoUser);
    }

    /**
     * {@code DELETE  /dodatni-info-users/:id} : delete the "id" dodatniInfoUser.
     *
     * @param id the id of the dodatniInfoUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dodatni-info-users/{id}")
    public ResponseEntity<Void> deleteDodatniInfoUser(@PathVariable Long id) {
        log.debug("REST request to delete DodatniInfoUser : {}", id);
        dodatniInfoUserRepository.deleteById(id);
        dodatniInfoUserSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dodatni-info-users?query=:query} : search for the dodatniInfoUser corresponding
     * to the query.
     *
     * @param query the query of the dodatniInfoUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/dodatni-info-users")
    public List<DodatniInfoUser> searchDodatniInfoUsers(@RequestParam String query) {
        log.debug("REST request to search DodatniInfoUsers for query {}", query);
        return StreamSupport
            .stream(dodatniInfoUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
