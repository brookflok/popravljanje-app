package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Kanton;
import com.damir.popravljanje.repository.KantonRepository;
import com.damir.popravljanje.repository.search.KantonSearchRepository;

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
 * Integration tests for the {@link KantonResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class KantonResourceIT {

    private static final String DEFAULT_IME_KANTONA = "AAAAAAAAAA";
    private static final String UPDATED_IME_KANTONA = "BBBBBBBBBB";

    @Autowired
    private KantonRepository kantonRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.KantonSearchRepositoryMockConfiguration
     */
    @Autowired
    private KantonSearchRepository mockKantonSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKantonMockMvc;

    private Kanton kanton;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanton createEntity(EntityManager em) {
        Kanton kanton = new Kanton()
            .imeKantona(DEFAULT_IME_KANTONA);
        return kanton;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanton createUpdatedEntity(EntityManager em) {
        Kanton kanton = new Kanton()
            .imeKantona(UPDATED_IME_KANTONA);
        return kanton;
    }

    @BeforeEach
    public void initTest() {
        kanton = createEntity(em);
    }

    @Test
    @Transactional
    public void createKanton() throws Exception {
        int databaseSizeBeforeCreate = kantonRepository.findAll().size();
        // Create the Kanton
        restKantonMockMvc.perform(post("/api/kantons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kanton)))
            .andExpect(status().isCreated());

        // Validate the Kanton in the database
        List<Kanton> kantonList = kantonRepository.findAll();
        assertThat(kantonList).hasSize(databaseSizeBeforeCreate + 1);
        Kanton testKanton = kantonList.get(kantonList.size() - 1);
        assertThat(testKanton.getImeKantona()).isEqualTo(DEFAULT_IME_KANTONA);

        // Validate the Kanton in Elasticsearch
        verify(mockKantonSearchRepository, times(1)).save(testKanton);
    }

    @Test
    @Transactional
    public void createKantonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kantonRepository.findAll().size();

        // Create the Kanton with an existing ID
        kanton.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKantonMockMvc.perform(post("/api/kantons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kanton)))
            .andExpect(status().isBadRequest());

        // Validate the Kanton in the database
        List<Kanton> kantonList = kantonRepository.findAll();
        assertThat(kantonList).hasSize(databaseSizeBeforeCreate);

        // Validate the Kanton in Elasticsearch
        verify(mockKantonSearchRepository, times(0)).save(kanton);
    }


    @Test
    @Transactional
    public void getAllKantons() throws Exception {
        // Initialize the database
        kantonRepository.saveAndFlush(kanton);

        // Get all the kantonList
        restKantonMockMvc.perform(get("/api/kantons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kanton.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeKantona").value(hasItem(DEFAULT_IME_KANTONA)));
    }
    
    @Test
    @Transactional
    public void getKanton() throws Exception {
        // Initialize the database
        kantonRepository.saveAndFlush(kanton);

        // Get the kanton
        restKantonMockMvc.perform(get("/api/kantons/{id}", kanton.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kanton.getId().intValue()))
            .andExpect(jsonPath("$.imeKantona").value(DEFAULT_IME_KANTONA));
    }
    @Test
    @Transactional
    public void getNonExistingKanton() throws Exception {
        // Get the kanton
        restKantonMockMvc.perform(get("/api/kantons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKanton() throws Exception {
        // Initialize the database
        kantonRepository.saveAndFlush(kanton);

        int databaseSizeBeforeUpdate = kantonRepository.findAll().size();

        // Update the kanton
        Kanton updatedKanton = kantonRepository.findById(kanton.getId()).get();
        // Disconnect from session so that the updates on updatedKanton are not directly saved in db
        em.detach(updatedKanton);
        updatedKanton
            .imeKantona(UPDATED_IME_KANTONA);

        restKantonMockMvc.perform(put("/api/kantons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKanton)))
            .andExpect(status().isOk());

        // Validate the Kanton in the database
        List<Kanton> kantonList = kantonRepository.findAll();
        assertThat(kantonList).hasSize(databaseSizeBeforeUpdate);
        Kanton testKanton = kantonList.get(kantonList.size() - 1);
        assertThat(testKanton.getImeKantona()).isEqualTo(UPDATED_IME_KANTONA);

        // Validate the Kanton in Elasticsearch
        verify(mockKantonSearchRepository, times(1)).save(testKanton);
    }

    @Test
    @Transactional
    public void updateNonExistingKanton() throws Exception {
        int databaseSizeBeforeUpdate = kantonRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKantonMockMvc.perform(put("/api/kantons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kanton)))
            .andExpect(status().isBadRequest());

        // Validate the Kanton in the database
        List<Kanton> kantonList = kantonRepository.findAll();
        assertThat(kantonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Kanton in Elasticsearch
        verify(mockKantonSearchRepository, times(0)).save(kanton);
    }

    @Test
    @Transactional
    public void deleteKanton() throws Exception {
        // Initialize the database
        kantonRepository.saveAndFlush(kanton);

        int databaseSizeBeforeDelete = kantonRepository.findAll().size();

        // Delete the kanton
        restKantonMockMvc.perform(delete("/api/kantons/{id}", kanton.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kanton> kantonList = kantonRepository.findAll();
        assertThat(kantonList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Kanton in Elasticsearch
        verify(mockKantonSearchRepository, times(1)).deleteById(kanton.getId());
    }

    @Test
    @Transactional
    public void searchKanton() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        kantonRepository.saveAndFlush(kanton);
        when(mockKantonSearchRepository.search(queryStringQuery("id:" + kanton.getId())))
            .thenReturn(Collections.singletonList(kanton));

        // Search the kanton
        restKantonMockMvc.perform(get("/api/_search/kantons?query=id:" + kanton.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kanton.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeKantona").value(hasItem(DEFAULT_IME_KANTONA)));
    }
}
