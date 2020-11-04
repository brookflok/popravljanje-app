package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.ProfilnaSlika;
import com.damir.popravljanje.repository.ProfilnaSlikaRepository;
import com.damir.popravljanje.repository.search.ProfilnaSlikaSearchRepository;

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
 * Integration tests for the {@link ProfilnaSlikaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfilnaSlikaResourceIT {

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProfilnaSlikaRepository profilnaSlikaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.ProfilnaSlikaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfilnaSlikaSearchRepository mockProfilnaSlikaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilnaSlikaMockMvc;

    private ProfilnaSlika profilnaSlika;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilnaSlika createEntity(EntityManager em) {
        ProfilnaSlika profilnaSlika = new ProfilnaSlika()
            .ime(DEFAULT_IME)
            .datum(DEFAULT_DATUM);
        return profilnaSlika;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilnaSlika createUpdatedEntity(EntityManager em) {
        ProfilnaSlika profilnaSlika = new ProfilnaSlika()
            .ime(UPDATED_IME)
            .datum(UPDATED_DATUM);
        return profilnaSlika;
    }

    @BeforeEach
    public void initTest() {
        profilnaSlika = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfilnaSlika() throws Exception {
        int databaseSizeBeforeCreate = profilnaSlikaRepository.findAll().size();
        // Create the ProfilnaSlika
        restProfilnaSlikaMockMvc.perform(post("/api/profilna-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilnaSlika)))
            .andExpect(status().isCreated());

        // Validate the ProfilnaSlika in the database
        List<ProfilnaSlika> profilnaSlikaList = profilnaSlikaRepository.findAll();
        assertThat(profilnaSlikaList).hasSize(databaseSizeBeforeCreate + 1);
        ProfilnaSlika testProfilnaSlika = profilnaSlikaList.get(profilnaSlikaList.size() - 1);
        assertThat(testProfilnaSlika.getIme()).isEqualTo(DEFAULT_IME);
        assertThat(testProfilnaSlika.getDatum()).isEqualTo(DEFAULT_DATUM);

        // Validate the ProfilnaSlika in Elasticsearch
        verify(mockProfilnaSlikaSearchRepository, times(1)).save(testProfilnaSlika);
    }

    @Test
    @Transactional
    public void createProfilnaSlikaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profilnaSlikaRepository.findAll().size();

        // Create the ProfilnaSlika with an existing ID
        profilnaSlika.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilnaSlikaMockMvc.perform(post("/api/profilna-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilnaSlika)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilnaSlika in the database
        List<ProfilnaSlika> profilnaSlikaList = profilnaSlikaRepository.findAll();
        assertThat(profilnaSlikaList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfilnaSlika in Elasticsearch
        verify(mockProfilnaSlikaSearchRepository, times(0)).save(profilnaSlika);
    }


    @Test
    @Transactional
    public void getAllProfilnaSlikas() throws Exception {
        // Initialize the database
        profilnaSlikaRepository.saveAndFlush(profilnaSlika);

        // Get all the profilnaSlikaList
        restProfilnaSlikaMockMvc.perform(get("/api/profilna-slikas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilnaSlika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }
    
    @Test
    @Transactional
    public void getProfilnaSlika() throws Exception {
        // Initialize the database
        profilnaSlikaRepository.saveAndFlush(profilnaSlika);

        // Get the profilnaSlika
        restProfilnaSlikaMockMvc.perform(get("/api/profilna-slikas/{id}", profilnaSlika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profilnaSlika.getId().intValue()))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProfilnaSlika() throws Exception {
        // Get the profilnaSlika
        restProfilnaSlikaMockMvc.perform(get("/api/profilna-slikas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfilnaSlika() throws Exception {
        // Initialize the database
        profilnaSlikaRepository.saveAndFlush(profilnaSlika);

        int databaseSizeBeforeUpdate = profilnaSlikaRepository.findAll().size();

        // Update the profilnaSlika
        ProfilnaSlika updatedProfilnaSlika = profilnaSlikaRepository.findById(profilnaSlika.getId()).get();
        // Disconnect from session so that the updates on updatedProfilnaSlika are not directly saved in db
        em.detach(updatedProfilnaSlika);
        updatedProfilnaSlika
            .ime(UPDATED_IME)
            .datum(UPDATED_DATUM);

        restProfilnaSlikaMockMvc.perform(put("/api/profilna-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfilnaSlika)))
            .andExpect(status().isOk());

        // Validate the ProfilnaSlika in the database
        List<ProfilnaSlika> profilnaSlikaList = profilnaSlikaRepository.findAll();
        assertThat(profilnaSlikaList).hasSize(databaseSizeBeforeUpdate);
        ProfilnaSlika testProfilnaSlika = profilnaSlikaList.get(profilnaSlikaList.size() - 1);
        assertThat(testProfilnaSlika.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testProfilnaSlika.getDatum()).isEqualTo(UPDATED_DATUM);

        // Validate the ProfilnaSlika in Elasticsearch
        verify(mockProfilnaSlikaSearchRepository, times(1)).save(testProfilnaSlika);
    }

    @Test
    @Transactional
    public void updateNonExistingProfilnaSlika() throws Exception {
        int databaseSizeBeforeUpdate = profilnaSlikaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilnaSlikaMockMvc.perform(put("/api/profilna-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilnaSlika)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilnaSlika in the database
        List<ProfilnaSlika> profilnaSlikaList = profilnaSlikaRepository.findAll();
        assertThat(profilnaSlikaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfilnaSlika in Elasticsearch
        verify(mockProfilnaSlikaSearchRepository, times(0)).save(profilnaSlika);
    }

    @Test
    @Transactional
    public void deleteProfilnaSlika() throws Exception {
        // Initialize the database
        profilnaSlikaRepository.saveAndFlush(profilnaSlika);

        int databaseSizeBeforeDelete = profilnaSlikaRepository.findAll().size();

        // Delete the profilnaSlika
        restProfilnaSlikaMockMvc.perform(delete("/api/profilna-slikas/{id}", profilnaSlika.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfilnaSlika> profilnaSlikaList = profilnaSlikaRepository.findAll();
        assertThat(profilnaSlikaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfilnaSlika in Elasticsearch
        verify(mockProfilnaSlikaSearchRepository, times(1)).deleteById(profilnaSlika.getId());
    }

    @Test
    @Transactional
    public void searchProfilnaSlika() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        profilnaSlikaRepository.saveAndFlush(profilnaSlika);
        when(mockProfilnaSlikaSearchRepository.search(queryStringQuery("id:" + profilnaSlika.getId())))
            .thenReturn(Collections.singletonList(profilnaSlika));

        // Search the profilnaSlika
        restProfilnaSlikaMockMvc.perform(get("/api/_search/profilna-slikas?query=id:" + profilnaSlika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilnaSlika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }
}
