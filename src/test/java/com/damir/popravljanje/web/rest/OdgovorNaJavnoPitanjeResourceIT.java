package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.OdgovorNaJavnoPitanje;
import com.damir.popravljanje.repository.OdgovorNaJavnoPitanjeRepository;
import com.damir.popravljanje.repository.search.OdgovorNaJavnoPitanjeSearchRepository;

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
 * Integration tests for the {@link OdgovorNaJavnoPitanjeResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OdgovorNaJavnoPitanjeResourceIT {

    private static final String DEFAULT_ODGOVOR = "AAAAAAAAAA";
    private static final String UPDATED_ODGOVOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PRIKAZ = false;
    private static final Boolean UPDATED_PRIKAZ = true;

    @Autowired
    private OdgovorNaJavnoPitanjeRepository odgovorNaJavnoPitanjeRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.OdgovorNaJavnoPitanjeSearchRepositoryMockConfiguration
     */
    @Autowired
    private OdgovorNaJavnoPitanjeSearchRepository mockOdgovorNaJavnoPitanjeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOdgovorNaJavnoPitanjeMockMvc;

    private OdgovorNaJavnoPitanje odgovorNaJavnoPitanje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OdgovorNaJavnoPitanje createEntity(EntityManager em) {
        OdgovorNaJavnoPitanje odgovorNaJavnoPitanje = new OdgovorNaJavnoPitanje()
            .odgovor(DEFAULT_ODGOVOR)
            .datum(DEFAULT_DATUM)
            .prikaz(DEFAULT_PRIKAZ);
        return odgovorNaJavnoPitanje;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OdgovorNaJavnoPitanje createUpdatedEntity(EntityManager em) {
        OdgovorNaJavnoPitanje odgovorNaJavnoPitanje = new OdgovorNaJavnoPitanje()
            .odgovor(UPDATED_ODGOVOR)
            .datum(UPDATED_DATUM)
            .prikaz(UPDATED_PRIKAZ);
        return odgovorNaJavnoPitanje;
    }

    @BeforeEach
    public void initTest() {
        odgovorNaJavnoPitanje = createEntity(em);
    }

    @Test
    @Transactional
    public void createOdgovorNaJavnoPitanje() throws Exception {
        int databaseSizeBeforeCreate = odgovorNaJavnoPitanjeRepository.findAll().size();
        // Create the OdgovorNaJavnoPitanje
        restOdgovorNaJavnoPitanjeMockMvc.perform(post("/api/odgovor-na-javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(odgovorNaJavnoPitanje)))
            .andExpect(status().isCreated());

        // Validate the OdgovorNaJavnoPitanje in the database
        List<OdgovorNaJavnoPitanje> odgovorNaJavnoPitanjeList = odgovorNaJavnoPitanjeRepository.findAll();
        assertThat(odgovorNaJavnoPitanjeList).hasSize(databaseSizeBeforeCreate + 1);
        OdgovorNaJavnoPitanje testOdgovorNaJavnoPitanje = odgovorNaJavnoPitanjeList.get(odgovorNaJavnoPitanjeList.size() - 1);
        assertThat(testOdgovorNaJavnoPitanje.getOdgovor()).isEqualTo(DEFAULT_ODGOVOR);
        assertThat(testOdgovorNaJavnoPitanje.getDatum()).isEqualTo(DEFAULT_DATUM);
        assertThat(testOdgovorNaJavnoPitanje.isPrikaz()).isEqualTo(DEFAULT_PRIKAZ);

        // Validate the OdgovorNaJavnoPitanje in Elasticsearch
        verify(mockOdgovorNaJavnoPitanjeSearchRepository, times(1)).save(testOdgovorNaJavnoPitanje);
    }

    @Test
    @Transactional
    public void createOdgovorNaJavnoPitanjeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = odgovorNaJavnoPitanjeRepository.findAll().size();

        // Create the OdgovorNaJavnoPitanje with an existing ID
        odgovorNaJavnoPitanje.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOdgovorNaJavnoPitanjeMockMvc.perform(post("/api/odgovor-na-javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(odgovorNaJavnoPitanje)))
            .andExpect(status().isBadRequest());

        // Validate the OdgovorNaJavnoPitanje in the database
        List<OdgovorNaJavnoPitanje> odgovorNaJavnoPitanjeList = odgovorNaJavnoPitanjeRepository.findAll();
        assertThat(odgovorNaJavnoPitanjeList).hasSize(databaseSizeBeforeCreate);

        // Validate the OdgovorNaJavnoPitanje in Elasticsearch
        verify(mockOdgovorNaJavnoPitanjeSearchRepository, times(0)).save(odgovorNaJavnoPitanje);
    }


    @Test
    @Transactional
    public void getAllOdgovorNaJavnoPitanjes() throws Exception {
        // Initialize the database
        odgovorNaJavnoPitanjeRepository.saveAndFlush(odgovorNaJavnoPitanje);

        // Get all the odgovorNaJavnoPitanjeList
        restOdgovorNaJavnoPitanjeMockMvc.perform(get("/api/odgovor-na-javno-pitanjes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(odgovorNaJavnoPitanje.getId().intValue())))
            .andExpect(jsonPath("$.[*].odgovor").value(hasItem(DEFAULT_ODGOVOR)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].prikaz").value(hasItem(DEFAULT_PRIKAZ.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOdgovorNaJavnoPitanje() throws Exception {
        // Initialize the database
        odgovorNaJavnoPitanjeRepository.saveAndFlush(odgovorNaJavnoPitanje);

        // Get the odgovorNaJavnoPitanje
        restOdgovorNaJavnoPitanjeMockMvc.perform(get("/api/odgovor-na-javno-pitanjes/{id}", odgovorNaJavnoPitanje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(odgovorNaJavnoPitanje.getId().intValue()))
            .andExpect(jsonPath("$.odgovor").value(DEFAULT_ODGOVOR))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()))
            .andExpect(jsonPath("$.prikaz").value(DEFAULT_PRIKAZ.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingOdgovorNaJavnoPitanje() throws Exception {
        // Get the odgovorNaJavnoPitanje
        restOdgovorNaJavnoPitanjeMockMvc.perform(get("/api/odgovor-na-javno-pitanjes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOdgovorNaJavnoPitanje() throws Exception {
        // Initialize the database
        odgovorNaJavnoPitanjeRepository.saveAndFlush(odgovorNaJavnoPitanje);

        int databaseSizeBeforeUpdate = odgovorNaJavnoPitanjeRepository.findAll().size();

        // Update the odgovorNaJavnoPitanje
        OdgovorNaJavnoPitanje updatedOdgovorNaJavnoPitanje = odgovorNaJavnoPitanjeRepository.findById(odgovorNaJavnoPitanje.getId()).get();
        // Disconnect from session so that the updates on updatedOdgovorNaJavnoPitanje are not directly saved in db
        em.detach(updatedOdgovorNaJavnoPitanje);
        updatedOdgovorNaJavnoPitanje
            .odgovor(UPDATED_ODGOVOR)
            .datum(UPDATED_DATUM)
            .prikaz(UPDATED_PRIKAZ);

        restOdgovorNaJavnoPitanjeMockMvc.perform(put("/api/odgovor-na-javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOdgovorNaJavnoPitanje)))
            .andExpect(status().isOk());

        // Validate the OdgovorNaJavnoPitanje in the database
        List<OdgovorNaJavnoPitanje> odgovorNaJavnoPitanjeList = odgovorNaJavnoPitanjeRepository.findAll();
        assertThat(odgovorNaJavnoPitanjeList).hasSize(databaseSizeBeforeUpdate);
        OdgovorNaJavnoPitanje testOdgovorNaJavnoPitanje = odgovorNaJavnoPitanjeList.get(odgovorNaJavnoPitanjeList.size() - 1);
        assertThat(testOdgovorNaJavnoPitanje.getOdgovor()).isEqualTo(UPDATED_ODGOVOR);
        assertThat(testOdgovorNaJavnoPitanje.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testOdgovorNaJavnoPitanje.isPrikaz()).isEqualTo(UPDATED_PRIKAZ);

        // Validate the OdgovorNaJavnoPitanje in Elasticsearch
        verify(mockOdgovorNaJavnoPitanjeSearchRepository, times(1)).save(testOdgovorNaJavnoPitanje);
    }

    @Test
    @Transactional
    public void updateNonExistingOdgovorNaJavnoPitanje() throws Exception {
        int databaseSizeBeforeUpdate = odgovorNaJavnoPitanjeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOdgovorNaJavnoPitanjeMockMvc.perform(put("/api/odgovor-na-javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(odgovorNaJavnoPitanje)))
            .andExpect(status().isBadRequest());

        // Validate the OdgovorNaJavnoPitanje in the database
        List<OdgovorNaJavnoPitanje> odgovorNaJavnoPitanjeList = odgovorNaJavnoPitanjeRepository.findAll();
        assertThat(odgovorNaJavnoPitanjeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OdgovorNaJavnoPitanje in Elasticsearch
        verify(mockOdgovorNaJavnoPitanjeSearchRepository, times(0)).save(odgovorNaJavnoPitanje);
    }

    @Test
    @Transactional
    public void deleteOdgovorNaJavnoPitanje() throws Exception {
        // Initialize the database
        odgovorNaJavnoPitanjeRepository.saveAndFlush(odgovorNaJavnoPitanje);

        int databaseSizeBeforeDelete = odgovorNaJavnoPitanjeRepository.findAll().size();

        // Delete the odgovorNaJavnoPitanje
        restOdgovorNaJavnoPitanjeMockMvc.perform(delete("/api/odgovor-na-javno-pitanjes/{id}", odgovorNaJavnoPitanje.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OdgovorNaJavnoPitanje> odgovorNaJavnoPitanjeList = odgovorNaJavnoPitanjeRepository.findAll();
        assertThat(odgovorNaJavnoPitanjeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OdgovorNaJavnoPitanje in Elasticsearch
        verify(mockOdgovorNaJavnoPitanjeSearchRepository, times(1)).deleteById(odgovorNaJavnoPitanje.getId());
    }

    @Test
    @Transactional
    public void searchOdgovorNaJavnoPitanje() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        odgovorNaJavnoPitanjeRepository.saveAndFlush(odgovorNaJavnoPitanje);
        when(mockOdgovorNaJavnoPitanjeSearchRepository.search(queryStringQuery("id:" + odgovorNaJavnoPitanje.getId())))
            .thenReturn(Collections.singletonList(odgovorNaJavnoPitanje));

        // Search the odgovorNaJavnoPitanje
        restOdgovorNaJavnoPitanjeMockMvc.perform(get("/api/_search/odgovor-na-javno-pitanjes?query=id:" + odgovorNaJavnoPitanje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(odgovorNaJavnoPitanje.getId().intValue())))
            .andExpect(jsonPath("$.[*].odgovor").value(hasItem(DEFAULT_ODGOVOR)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].prikaz").value(hasItem(DEFAULT_PRIKAZ.booleanValue())));
    }
}
