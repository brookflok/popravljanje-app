package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Poruka;
import com.damir.popravljanje.repository.PorukaRepository;
import com.damir.popravljanje.repository.search.PorukaSearchRepository;

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
 * Integration tests for the {@link PorukaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PorukaResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_POSTOJI = false;
    private static final Boolean UPDATED_POSTOJI = true;

    @Autowired
    private PorukaRepository porukaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.PorukaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PorukaSearchRepository mockPorukaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPorukaMockMvc;

    private Poruka poruka;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poruka createEntity(EntityManager em) {
        Poruka poruka = new Poruka()
            .text(DEFAULT_TEXT)
            .datum(DEFAULT_DATUM)
            .postoji(DEFAULT_POSTOJI);
        return poruka;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poruka createUpdatedEntity(EntityManager em) {
        Poruka poruka = new Poruka()
            .text(UPDATED_TEXT)
            .datum(UPDATED_DATUM)
            .postoji(UPDATED_POSTOJI);
        return poruka;
    }

    @BeforeEach
    public void initTest() {
        poruka = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoruka() throws Exception {
        int databaseSizeBeforeCreate = porukaRepository.findAll().size();
        // Create the Poruka
        restPorukaMockMvc.perform(post("/api/porukas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(poruka)))
            .andExpect(status().isCreated());

        // Validate the Poruka in the database
        List<Poruka> porukaList = porukaRepository.findAll();
        assertThat(porukaList).hasSize(databaseSizeBeforeCreate + 1);
        Poruka testPoruka = porukaList.get(porukaList.size() - 1);
        assertThat(testPoruka.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testPoruka.getDatum()).isEqualTo(DEFAULT_DATUM);
        assertThat(testPoruka.isPostoji()).isEqualTo(DEFAULT_POSTOJI);

        // Validate the Poruka in Elasticsearch
        verify(mockPorukaSearchRepository, times(1)).save(testPoruka);
    }

    @Test
    @Transactional
    public void createPorukaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = porukaRepository.findAll().size();

        // Create the Poruka with an existing ID
        poruka.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPorukaMockMvc.perform(post("/api/porukas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(poruka)))
            .andExpect(status().isBadRequest());

        // Validate the Poruka in the database
        List<Poruka> porukaList = porukaRepository.findAll();
        assertThat(porukaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Poruka in Elasticsearch
        verify(mockPorukaSearchRepository, times(0)).save(poruka);
    }


    @Test
    @Transactional
    public void getAllPorukas() throws Exception {
        // Initialize the database
        porukaRepository.saveAndFlush(poruka);

        // Get all the porukaList
        restPorukaMockMvc.perform(get("/api/porukas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poruka.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPoruka() throws Exception {
        // Initialize the database
        porukaRepository.saveAndFlush(poruka);

        // Get the poruka
        restPorukaMockMvc.perform(get("/api/porukas/{id}", poruka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poruka.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()))
            .andExpect(jsonPath("$.postoji").value(DEFAULT_POSTOJI.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPoruka() throws Exception {
        // Get the poruka
        restPorukaMockMvc.perform(get("/api/porukas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoruka() throws Exception {
        // Initialize the database
        porukaRepository.saveAndFlush(poruka);

        int databaseSizeBeforeUpdate = porukaRepository.findAll().size();

        // Update the poruka
        Poruka updatedPoruka = porukaRepository.findById(poruka.getId()).get();
        // Disconnect from session so that the updates on updatedPoruka are not directly saved in db
        em.detach(updatedPoruka);
        updatedPoruka
            .text(UPDATED_TEXT)
            .datum(UPDATED_DATUM)
            .postoji(UPDATED_POSTOJI);

        restPorukaMockMvc.perform(put("/api/porukas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPoruka)))
            .andExpect(status().isOk());

        // Validate the Poruka in the database
        List<Poruka> porukaList = porukaRepository.findAll();
        assertThat(porukaList).hasSize(databaseSizeBeforeUpdate);
        Poruka testPoruka = porukaList.get(porukaList.size() - 1);
        assertThat(testPoruka.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPoruka.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testPoruka.isPostoji()).isEqualTo(UPDATED_POSTOJI);

        // Validate the Poruka in Elasticsearch
        verify(mockPorukaSearchRepository, times(1)).save(testPoruka);
    }

    @Test
    @Transactional
    public void updateNonExistingPoruka() throws Exception {
        int databaseSizeBeforeUpdate = porukaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPorukaMockMvc.perform(put("/api/porukas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(poruka)))
            .andExpect(status().isBadRequest());

        // Validate the Poruka in the database
        List<Poruka> porukaList = porukaRepository.findAll();
        assertThat(porukaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Poruka in Elasticsearch
        verify(mockPorukaSearchRepository, times(0)).save(poruka);
    }

    @Test
    @Transactional
    public void deletePoruka() throws Exception {
        // Initialize the database
        porukaRepository.saveAndFlush(poruka);

        int databaseSizeBeforeDelete = porukaRepository.findAll().size();

        // Delete the poruka
        restPorukaMockMvc.perform(delete("/api/porukas/{id}", poruka.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poruka> porukaList = porukaRepository.findAll();
        assertThat(porukaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Poruka in Elasticsearch
        verify(mockPorukaSearchRepository, times(1)).deleteById(poruka.getId());
    }

    @Test
    @Transactional
    public void searchPoruka() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        porukaRepository.saveAndFlush(poruka);
        when(mockPorukaSearchRepository.search(queryStringQuery("id:" + poruka.getId())))
            .thenReturn(Collections.singletonList(poruka));

        // Search the poruka
        restPorukaMockMvc.perform(get("/api/_search/porukas?query=id:" + poruka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poruka.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())));
    }
}
