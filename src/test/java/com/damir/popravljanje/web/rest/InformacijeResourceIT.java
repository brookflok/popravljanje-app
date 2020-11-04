package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Informacije;
import com.damir.popravljanje.repository.InformacijeRepository;
import com.damir.popravljanje.repository.search.InformacijeSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InformacijeResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InformacijeResourceIT {

    private static final String DEFAULT_VRSTA_OGLASA = "AAAAAAAAAA";
    private static final String UPDATED_VRSTA_OGLASA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM_OBJAVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM_OBJAVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_BROJ_PREGLEDA = 1;
    private static final Integer UPDATED_BROJ_PREGLEDA = 2;

    @Autowired
    private InformacijeRepository informacijeRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.InformacijeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InformacijeSearchRepository mockInformacijeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformacijeMockMvc;

    private Informacije informacije;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informacije createEntity(EntityManager em) {
        Informacije informacije = new Informacije()
            .vrstaOglasa(DEFAULT_VRSTA_OGLASA)
            .datumObjave(DEFAULT_DATUM_OBJAVE)
            .brojPregleda(DEFAULT_BROJ_PREGLEDA);
        return informacije;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informacije createUpdatedEntity(EntityManager em) {
        Informacije informacije = new Informacije()
            .vrstaOglasa(UPDATED_VRSTA_OGLASA)
            .datumObjave(UPDATED_DATUM_OBJAVE)
            .brojPregleda(UPDATED_BROJ_PREGLEDA);
        return informacije;
    }

    @BeforeEach
    public void initTest() {
        informacije = createEntity(em);
    }

    @Test
    @Transactional
    public void createInformacije() throws Exception {
        int databaseSizeBeforeCreate = informacijeRepository.findAll().size();
        // Create the Informacije
        restInformacijeMockMvc.perform(post("/api/informacijes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informacije)))
            .andExpect(status().isCreated());

        // Validate the Informacije in the database
        List<Informacije> informacijeList = informacijeRepository.findAll();
        assertThat(informacijeList).hasSize(databaseSizeBeforeCreate + 1);
        Informacije testInformacije = informacijeList.get(informacijeList.size() - 1);
        assertThat(testInformacije.getVrstaOglasa()).isEqualTo(DEFAULT_VRSTA_OGLASA);
        assertThat(testInformacije.getDatumObjave()).isEqualTo(DEFAULT_DATUM_OBJAVE);
        assertThat(testInformacije.getBrojPregleda()).isEqualTo(DEFAULT_BROJ_PREGLEDA);

        // Validate the Informacije in Elasticsearch
        verify(mockInformacijeSearchRepository, times(1)).save(testInformacije);
    }

    @Test
    @Transactional
    public void createInformacijeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = informacijeRepository.findAll().size();

        // Create the Informacije with an existing ID
        informacije.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformacijeMockMvc.perform(post("/api/informacijes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informacije)))
            .andExpect(status().isBadRequest());

        // Validate the Informacije in the database
        List<Informacije> informacijeList = informacijeRepository.findAll();
        assertThat(informacijeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Informacije in Elasticsearch
        verify(mockInformacijeSearchRepository, times(0)).save(informacije);
    }


    @Test
    @Transactional
    public void getAllInformacijes() throws Exception {
        // Initialize the database
        informacijeRepository.saveAndFlush(informacije);

        // Get all the informacijeList
        restInformacijeMockMvc.perform(get("/api/informacijes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].vrstaOglasa").value(hasItem(DEFAULT_VRSTA_OGLASA)))
            .andExpect(jsonPath("$.[*].datumObjave").value(hasItem(DEFAULT_DATUM_OBJAVE.toString())))
            .andExpect(jsonPath("$.[*].brojPregleda").value(hasItem(DEFAULT_BROJ_PREGLEDA)));
    }
    
    @Test
    @Transactional
    public void getInformacije() throws Exception {
        // Initialize the database
        informacijeRepository.saveAndFlush(informacije);

        // Get the informacije
        restInformacijeMockMvc.perform(get("/api/informacijes/{id}", informacije.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informacije.getId().intValue()))
            .andExpect(jsonPath("$.vrstaOglasa").value(DEFAULT_VRSTA_OGLASA))
            .andExpect(jsonPath("$.datumObjave").value(DEFAULT_DATUM_OBJAVE.toString()))
            .andExpect(jsonPath("$.brojPregleda").value(DEFAULT_BROJ_PREGLEDA));
    }
    @Test
    @Transactional
    public void getNonExistingInformacije() throws Exception {
        // Get the informacije
        restInformacijeMockMvc.perform(get("/api/informacijes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInformacije() throws Exception {
        // Initialize the database
        informacijeRepository.saveAndFlush(informacije);

        int databaseSizeBeforeUpdate = informacijeRepository.findAll().size();

        // Update the informacije
        Informacije updatedInformacije = informacijeRepository.findById(informacije.getId()).get();
        // Disconnect from session so that the updates on updatedInformacije are not directly saved in db
        em.detach(updatedInformacije);
        updatedInformacije
            .vrstaOglasa(UPDATED_VRSTA_OGLASA)
            .datumObjave(UPDATED_DATUM_OBJAVE)
            .brojPregleda(UPDATED_BROJ_PREGLEDA);

        restInformacijeMockMvc.perform(put("/api/informacijes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInformacije)))
            .andExpect(status().isOk());

        // Validate the Informacije in the database
        List<Informacije> informacijeList = informacijeRepository.findAll();
        assertThat(informacijeList).hasSize(databaseSizeBeforeUpdate);
        Informacije testInformacije = informacijeList.get(informacijeList.size() - 1);
        assertThat(testInformacije.getVrstaOglasa()).isEqualTo(UPDATED_VRSTA_OGLASA);
        assertThat(testInformacije.getDatumObjave()).isEqualTo(UPDATED_DATUM_OBJAVE);
        assertThat(testInformacije.getBrojPregleda()).isEqualTo(UPDATED_BROJ_PREGLEDA);

        // Validate the Informacije in Elasticsearch
        verify(mockInformacijeSearchRepository, times(1)).save(testInformacije);
    }

    @Test
    @Transactional
    public void updateNonExistingInformacije() throws Exception {
        int databaseSizeBeforeUpdate = informacijeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformacijeMockMvc.perform(put("/api/informacijes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informacije)))
            .andExpect(status().isBadRequest());

        // Validate the Informacije in the database
        List<Informacije> informacijeList = informacijeRepository.findAll();
        assertThat(informacijeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Informacije in Elasticsearch
        verify(mockInformacijeSearchRepository, times(0)).save(informacije);
    }

    @Test
    @Transactional
    public void deleteInformacije() throws Exception {
        // Initialize the database
        informacijeRepository.saveAndFlush(informacije);

        int databaseSizeBeforeDelete = informacijeRepository.findAll().size();

        // Delete the informacije
        restInformacijeMockMvc.perform(delete("/api/informacijes/{id}", informacije.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Informacije> informacijeList = informacijeRepository.findAll();
        assertThat(informacijeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Informacije in Elasticsearch
        verify(mockInformacijeSearchRepository, times(1)).deleteById(informacije.getId());
    }

    @Test
    @Transactional
    public void searchInformacije() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        informacijeRepository.saveAndFlush(informacije);
        when(mockInformacijeSearchRepository.search(queryStringQuery("id:" + informacije.getId())))
            .thenReturn(Collections.singletonList(informacije));

        // Search the informacije
        restInformacijeMockMvc.perform(get("/api/_search/informacijes?query=id:" + informacije.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].vrstaOglasa").value(hasItem(DEFAULT_VRSTA_OGLASA)))
            .andExpect(jsonPath("$.[*].datumObjave").value(hasItem(DEFAULT_DATUM_OBJAVE.toString())))
            .andExpect(jsonPath("$.[*].brojPregleda").value(hasItem(DEFAULT_BROJ_PREGLEDA)));
    }
}
