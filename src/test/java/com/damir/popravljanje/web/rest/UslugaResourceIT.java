package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Usluga;
import com.damir.popravljanje.repository.UslugaRepository;
import com.damir.popravljanje.repository.search.UslugaSearchRepository;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UslugaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UslugaResourceIT {

    private static final Double DEFAULT_CIJENA = 1D;
    private static final Double UPDATED_CIJENA = 2D;

    @Autowired
    private UslugaRepository uslugaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.UslugaSearchRepositoryMockConfiguration
     */
    @Autowired
    private UslugaSearchRepository mockUslugaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUslugaMockMvc;

    private Usluga usluga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usluga createEntity(EntityManager em) {
        Usluga usluga = new Usluga()
            .cijena(DEFAULT_CIJENA);
        return usluga;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usluga createUpdatedEntity(EntityManager em) {
        Usluga usluga = new Usluga()
            .cijena(UPDATED_CIJENA);
        return usluga;
    }

    @BeforeEach
    public void initTest() {
        usluga = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsluga() throws Exception {
        int databaseSizeBeforeCreate = uslugaRepository.findAll().size();
        // Create the Usluga
        restUslugaMockMvc.perform(post("/api/uslugas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(usluga)))
            .andExpect(status().isCreated());

        // Validate the Usluga in the database
        List<Usluga> uslugaList = uslugaRepository.findAll();
        assertThat(uslugaList).hasSize(databaseSizeBeforeCreate + 1);
        Usluga testUsluga = uslugaList.get(uslugaList.size() - 1);
        assertThat(testUsluga.getCijena()).isEqualTo(DEFAULT_CIJENA);

        // Validate the Usluga in Elasticsearch
        verify(mockUslugaSearchRepository, times(1)).save(testUsluga);
    }

    @Test
    @Transactional
    public void createUslugaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uslugaRepository.findAll().size();

        // Create the Usluga with an existing ID
        usluga.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUslugaMockMvc.perform(post("/api/uslugas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(usluga)))
            .andExpect(status().isBadRequest());

        // Validate the Usluga in the database
        List<Usluga> uslugaList = uslugaRepository.findAll();
        assertThat(uslugaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Usluga in Elasticsearch
        verify(mockUslugaSearchRepository, times(0)).save(usluga);
    }


    @Test
    @Transactional
    public void getAllUslugas() throws Exception {
        // Initialize the database
        uslugaRepository.saveAndFlush(usluga);

        // Get all the uslugaList
        restUslugaMockMvc.perform(get("/api/uslugas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usluga.getId().intValue())))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getUsluga() throws Exception {
        // Initialize the database
        uslugaRepository.saveAndFlush(usluga);

        // Get the usluga
        restUslugaMockMvc.perform(get("/api/uslugas/{id}", usluga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usluga.getId().intValue()))
            .andExpect(jsonPath("$.cijena").value(DEFAULT_CIJENA.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingUsluga() throws Exception {
        // Get the usluga
        restUslugaMockMvc.perform(get("/api/uslugas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsluga() throws Exception {
        // Initialize the database
        uslugaRepository.saveAndFlush(usluga);

        int databaseSizeBeforeUpdate = uslugaRepository.findAll().size();

        // Update the usluga
        Usluga updatedUsluga = uslugaRepository.findById(usluga.getId()).get();
        // Disconnect from session so that the updates on updatedUsluga are not directly saved in db
        em.detach(updatedUsluga);
        updatedUsluga
            .cijena(UPDATED_CIJENA);

        restUslugaMockMvc.perform(put("/api/uslugas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUsluga)))
            .andExpect(status().isOk());

        // Validate the Usluga in the database
        List<Usluga> uslugaList = uslugaRepository.findAll();
        assertThat(uslugaList).hasSize(databaseSizeBeforeUpdate);
        Usluga testUsluga = uslugaList.get(uslugaList.size() - 1);
        assertThat(testUsluga.getCijena()).isEqualTo(UPDATED_CIJENA);

        // Validate the Usluga in Elasticsearch
        verify(mockUslugaSearchRepository, times(1)).save(testUsluga);
    }

    @Test
    @Transactional
    public void updateNonExistingUsluga() throws Exception {
        int databaseSizeBeforeUpdate = uslugaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUslugaMockMvc.perform(put("/api/uslugas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(usluga)))
            .andExpect(status().isBadRequest());

        // Validate the Usluga in the database
        List<Usluga> uslugaList = uslugaRepository.findAll();
        assertThat(uslugaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Usluga in Elasticsearch
        verify(mockUslugaSearchRepository, times(0)).save(usluga);
    }

    @Test
    @Transactional
    public void deleteUsluga() throws Exception {
        // Initialize the database
        uslugaRepository.saveAndFlush(usluga);

        int databaseSizeBeforeDelete = uslugaRepository.findAll().size();

        // Delete the usluga
        restUslugaMockMvc.perform(delete("/api/uslugas/{id}", usluga.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usluga> uslugaList = uslugaRepository.findAll();
        assertThat(uslugaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Usluga in Elasticsearch
        verify(mockUslugaSearchRepository, times(1)).deleteById(usluga.getId());
    }

    @Test
    @Transactional
    public void searchUsluga() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        uslugaRepository.saveAndFlush(usluga);
        when(mockUslugaSearchRepository.search(queryStringQuery("id:" + usluga.getId())))
            .thenReturn(Collections.singletonList(usluga));

        // Search the usluga
        restUslugaMockMvc.perform(get("/api/_search/uslugas?query=id:" + usluga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usluga.getId().intValue())))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())));
    }
}
