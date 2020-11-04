package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Potreba;
import com.damir.popravljanje.repository.PotrebaRepository;
import com.damir.popravljanje.repository.search.PotrebaSearchRepository;

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
 * Integration tests for the {@link PotrebaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PotrebaResourceIT {

    private static final Double DEFAULT_CIJENA_MIN = 1D;
    private static final Double UPDATED_CIJENA_MIN = 2D;

    private static final Double DEFAULT_CIJENA_MAX = 1D;
    private static final Double UPDATED_CIJENA_MAX = 2D;

    @Autowired
    private PotrebaRepository potrebaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.PotrebaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PotrebaSearchRepository mockPotrebaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPotrebaMockMvc;

    private Potreba potreba;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Potreba createEntity(EntityManager em) {
        Potreba potreba = new Potreba()
            .cijenaMin(DEFAULT_CIJENA_MIN)
            .cijenaMax(DEFAULT_CIJENA_MAX);
        return potreba;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Potreba createUpdatedEntity(EntityManager em) {
        Potreba potreba = new Potreba()
            .cijenaMin(UPDATED_CIJENA_MIN)
            .cijenaMax(UPDATED_CIJENA_MAX);
        return potreba;
    }

    @BeforeEach
    public void initTest() {
        potreba = createEntity(em);
    }

    @Test
    @Transactional
    public void createPotreba() throws Exception {
        int databaseSizeBeforeCreate = potrebaRepository.findAll().size();
        // Create the Potreba
        restPotrebaMockMvc.perform(post("/api/potrebas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreba)))
            .andExpect(status().isCreated());

        // Validate the Potreba in the database
        List<Potreba> potrebaList = potrebaRepository.findAll();
        assertThat(potrebaList).hasSize(databaseSizeBeforeCreate + 1);
        Potreba testPotreba = potrebaList.get(potrebaList.size() - 1);
        assertThat(testPotreba.getCijenaMin()).isEqualTo(DEFAULT_CIJENA_MIN);
        assertThat(testPotreba.getCijenaMax()).isEqualTo(DEFAULT_CIJENA_MAX);

        // Validate the Potreba in Elasticsearch
        verify(mockPotrebaSearchRepository, times(1)).save(testPotreba);
    }

    @Test
    @Transactional
    public void createPotrebaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = potrebaRepository.findAll().size();

        // Create the Potreba with an existing ID
        potreba.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotrebaMockMvc.perform(post("/api/potrebas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreba)))
            .andExpect(status().isBadRequest());

        // Validate the Potreba in the database
        List<Potreba> potrebaList = potrebaRepository.findAll();
        assertThat(potrebaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Potreba in Elasticsearch
        verify(mockPotrebaSearchRepository, times(0)).save(potreba);
    }


    @Test
    @Transactional
    public void getAllPotrebas() throws Exception {
        // Initialize the database
        potrebaRepository.saveAndFlush(potreba);

        // Get all the potrebaList
        restPotrebaMockMvc.perform(get("/api/potrebas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potreba.getId().intValue())))
            .andExpect(jsonPath("$.[*].cijenaMin").value(hasItem(DEFAULT_CIJENA_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].cijenaMax").value(hasItem(DEFAULT_CIJENA_MAX.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPotreba() throws Exception {
        // Initialize the database
        potrebaRepository.saveAndFlush(potreba);

        // Get the potreba
        restPotrebaMockMvc.perform(get("/api/potrebas/{id}", potreba.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(potreba.getId().intValue()))
            .andExpect(jsonPath("$.cijenaMin").value(DEFAULT_CIJENA_MIN.doubleValue()))
            .andExpect(jsonPath("$.cijenaMax").value(DEFAULT_CIJENA_MAX.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPotreba() throws Exception {
        // Get the potreba
        restPotrebaMockMvc.perform(get("/api/potrebas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePotreba() throws Exception {
        // Initialize the database
        potrebaRepository.saveAndFlush(potreba);

        int databaseSizeBeforeUpdate = potrebaRepository.findAll().size();

        // Update the potreba
        Potreba updatedPotreba = potrebaRepository.findById(potreba.getId()).get();
        // Disconnect from session so that the updates on updatedPotreba are not directly saved in db
        em.detach(updatedPotreba);
        updatedPotreba
            .cijenaMin(UPDATED_CIJENA_MIN)
            .cijenaMax(UPDATED_CIJENA_MAX);

        restPotrebaMockMvc.perform(put("/api/potrebas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPotreba)))
            .andExpect(status().isOk());

        // Validate the Potreba in the database
        List<Potreba> potrebaList = potrebaRepository.findAll();
        assertThat(potrebaList).hasSize(databaseSizeBeforeUpdate);
        Potreba testPotreba = potrebaList.get(potrebaList.size() - 1);
        assertThat(testPotreba.getCijenaMin()).isEqualTo(UPDATED_CIJENA_MIN);
        assertThat(testPotreba.getCijenaMax()).isEqualTo(UPDATED_CIJENA_MAX);

        // Validate the Potreba in Elasticsearch
        verify(mockPotrebaSearchRepository, times(1)).save(testPotreba);
    }

    @Test
    @Transactional
    public void updateNonExistingPotreba() throws Exception {
        int databaseSizeBeforeUpdate = potrebaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotrebaMockMvc.perform(put("/api/potrebas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreba)))
            .andExpect(status().isBadRequest());

        // Validate the Potreba in the database
        List<Potreba> potrebaList = potrebaRepository.findAll();
        assertThat(potrebaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Potreba in Elasticsearch
        verify(mockPotrebaSearchRepository, times(0)).save(potreba);
    }

    @Test
    @Transactional
    public void deletePotreba() throws Exception {
        // Initialize the database
        potrebaRepository.saveAndFlush(potreba);

        int databaseSizeBeforeDelete = potrebaRepository.findAll().size();

        // Delete the potreba
        restPotrebaMockMvc.perform(delete("/api/potrebas/{id}", potreba.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Potreba> potrebaList = potrebaRepository.findAll();
        assertThat(potrebaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Potreba in Elasticsearch
        verify(mockPotrebaSearchRepository, times(1)).deleteById(potreba.getId());
    }

    @Test
    @Transactional
    public void searchPotreba() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        potrebaRepository.saveAndFlush(potreba);
        when(mockPotrebaSearchRepository.search(queryStringQuery("id:" + potreba.getId())))
            .thenReturn(Collections.singletonList(potreba));

        // Search the potreba
        restPotrebaMockMvc.perform(get("/api/_search/potrebas?query=id:" + potreba.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potreba.getId().intValue())))
            .andExpect(jsonPath("$.[*].cijenaMin").value(hasItem(DEFAULT_CIJENA_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].cijenaMax").value(hasItem(DEFAULT_CIJENA_MAX.doubleValue())));
    }
}
