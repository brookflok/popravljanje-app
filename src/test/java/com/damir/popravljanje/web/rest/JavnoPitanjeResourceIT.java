package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.JavnoPitanje;
import com.damir.popravljanje.repository.JavnoPitanjeRepository;
import com.damir.popravljanje.repository.search.JavnoPitanjeSearchRepository;

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
 * Integration tests for the {@link JavnoPitanjeResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class JavnoPitanjeResourceIT {

    private static final String DEFAULT_PITANJE = "AAAAAAAAAA";
    private static final String UPDATED_PITANJE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PRIKAZ = false;
    private static final Boolean UPDATED_PRIKAZ = true;

    @Autowired
    private JavnoPitanjeRepository javnoPitanjeRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.JavnoPitanjeSearchRepositoryMockConfiguration
     */
    @Autowired
    private JavnoPitanjeSearchRepository mockJavnoPitanjeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJavnoPitanjeMockMvc;

    private JavnoPitanje javnoPitanje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JavnoPitanje createEntity(EntityManager em) {
        JavnoPitanje javnoPitanje = new JavnoPitanje()
            .pitanje(DEFAULT_PITANJE)
            .datum(DEFAULT_DATUM)
            .prikaz(DEFAULT_PRIKAZ);
        return javnoPitanje;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JavnoPitanje createUpdatedEntity(EntityManager em) {
        JavnoPitanje javnoPitanje = new JavnoPitanje()
            .pitanje(UPDATED_PITANJE)
            .datum(UPDATED_DATUM)
            .prikaz(UPDATED_PRIKAZ);
        return javnoPitanje;
    }

    @BeforeEach
    public void initTest() {
        javnoPitanje = createEntity(em);
    }

    @Test
    @Transactional
    public void createJavnoPitanje() throws Exception {
        int databaseSizeBeforeCreate = javnoPitanjeRepository.findAll().size();
        // Create the JavnoPitanje
        restJavnoPitanjeMockMvc.perform(post("/api/javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(javnoPitanje)))
            .andExpect(status().isCreated());

        // Validate the JavnoPitanje in the database
        List<JavnoPitanje> javnoPitanjeList = javnoPitanjeRepository.findAll();
        assertThat(javnoPitanjeList).hasSize(databaseSizeBeforeCreate + 1);
        JavnoPitanje testJavnoPitanje = javnoPitanjeList.get(javnoPitanjeList.size() - 1);
        assertThat(testJavnoPitanje.getPitanje()).isEqualTo(DEFAULT_PITANJE);
        assertThat(testJavnoPitanje.getDatum()).isEqualTo(DEFAULT_DATUM);
        assertThat(testJavnoPitanje.isPrikaz()).isEqualTo(DEFAULT_PRIKAZ);

        // Validate the JavnoPitanje in Elasticsearch
        verify(mockJavnoPitanjeSearchRepository, times(1)).save(testJavnoPitanje);
    }

    @Test
    @Transactional
    public void createJavnoPitanjeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = javnoPitanjeRepository.findAll().size();

        // Create the JavnoPitanje with an existing ID
        javnoPitanje.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJavnoPitanjeMockMvc.perform(post("/api/javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(javnoPitanje)))
            .andExpect(status().isBadRequest());

        // Validate the JavnoPitanje in the database
        List<JavnoPitanje> javnoPitanjeList = javnoPitanjeRepository.findAll();
        assertThat(javnoPitanjeList).hasSize(databaseSizeBeforeCreate);

        // Validate the JavnoPitanje in Elasticsearch
        verify(mockJavnoPitanjeSearchRepository, times(0)).save(javnoPitanje);
    }


    @Test
    @Transactional
    public void getAllJavnoPitanjes() throws Exception {
        // Initialize the database
        javnoPitanjeRepository.saveAndFlush(javnoPitanje);

        // Get all the javnoPitanjeList
        restJavnoPitanjeMockMvc.perform(get("/api/javno-pitanjes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(javnoPitanje.getId().intValue())))
            .andExpect(jsonPath("$.[*].pitanje").value(hasItem(DEFAULT_PITANJE)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].prikaz").value(hasItem(DEFAULT_PRIKAZ.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getJavnoPitanje() throws Exception {
        // Initialize the database
        javnoPitanjeRepository.saveAndFlush(javnoPitanje);

        // Get the javnoPitanje
        restJavnoPitanjeMockMvc.perform(get("/api/javno-pitanjes/{id}", javnoPitanje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(javnoPitanje.getId().intValue()))
            .andExpect(jsonPath("$.pitanje").value(DEFAULT_PITANJE))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()))
            .andExpect(jsonPath("$.prikaz").value(DEFAULT_PRIKAZ.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingJavnoPitanje() throws Exception {
        // Get the javnoPitanje
        restJavnoPitanjeMockMvc.perform(get("/api/javno-pitanjes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJavnoPitanje() throws Exception {
        // Initialize the database
        javnoPitanjeRepository.saveAndFlush(javnoPitanje);

        int databaseSizeBeforeUpdate = javnoPitanjeRepository.findAll().size();

        // Update the javnoPitanje
        JavnoPitanje updatedJavnoPitanje = javnoPitanjeRepository.findById(javnoPitanje.getId()).get();
        // Disconnect from session so that the updates on updatedJavnoPitanje are not directly saved in db
        em.detach(updatedJavnoPitanje);
        updatedJavnoPitanje
            .pitanje(UPDATED_PITANJE)
            .datum(UPDATED_DATUM)
            .prikaz(UPDATED_PRIKAZ);

        restJavnoPitanjeMockMvc.perform(put("/api/javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJavnoPitanje)))
            .andExpect(status().isOk());

        // Validate the JavnoPitanje in the database
        List<JavnoPitanje> javnoPitanjeList = javnoPitanjeRepository.findAll();
        assertThat(javnoPitanjeList).hasSize(databaseSizeBeforeUpdate);
        JavnoPitanje testJavnoPitanje = javnoPitanjeList.get(javnoPitanjeList.size() - 1);
        assertThat(testJavnoPitanje.getPitanje()).isEqualTo(UPDATED_PITANJE);
        assertThat(testJavnoPitanje.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testJavnoPitanje.isPrikaz()).isEqualTo(UPDATED_PRIKAZ);

        // Validate the JavnoPitanje in Elasticsearch
        verify(mockJavnoPitanjeSearchRepository, times(1)).save(testJavnoPitanje);
    }

    @Test
    @Transactional
    public void updateNonExistingJavnoPitanje() throws Exception {
        int databaseSizeBeforeUpdate = javnoPitanjeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJavnoPitanjeMockMvc.perform(put("/api/javno-pitanjes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(javnoPitanje)))
            .andExpect(status().isBadRequest());

        // Validate the JavnoPitanje in the database
        List<JavnoPitanje> javnoPitanjeList = javnoPitanjeRepository.findAll();
        assertThat(javnoPitanjeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JavnoPitanje in Elasticsearch
        verify(mockJavnoPitanjeSearchRepository, times(0)).save(javnoPitanje);
    }

    @Test
    @Transactional
    public void deleteJavnoPitanje() throws Exception {
        // Initialize the database
        javnoPitanjeRepository.saveAndFlush(javnoPitanje);

        int databaseSizeBeforeDelete = javnoPitanjeRepository.findAll().size();

        // Delete the javnoPitanje
        restJavnoPitanjeMockMvc.perform(delete("/api/javno-pitanjes/{id}", javnoPitanje.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JavnoPitanje> javnoPitanjeList = javnoPitanjeRepository.findAll();
        assertThat(javnoPitanjeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JavnoPitanje in Elasticsearch
        verify(mockJavnoPitanjeSearchRepository, times(1)).deleteById(javnoPitanje.getId());
    }

    @Test
    @Transactional
    public void searchJavnoPitanje() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        javnoPitanjeRepository.saveAndFlush(javnoPitanje);
        when(mockJavnoPitanjeSearchRepository.search(queryStringQuery("id:" + javnoPitanje.getId())))
            .thenReturn(Collections.singletonList(javnoPitanje));

        // Search the javnoPitanje
        restJavnoPitanjeMockMvc.perform(get("/api/_search/javno-pitanjes?query=id:" + javnoPitanje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(javnoPitanje.getId().intValue())))
            .andExpect(jsonPath("$.[*].pitanje").value(hasItem(DEFAULT_PITANJE)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].prikaz").value(hasItem(DEFAULT_PRIKAZ.booleanValue())));
    }
}
