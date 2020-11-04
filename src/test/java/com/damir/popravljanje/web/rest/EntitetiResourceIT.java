package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Entiteti;
import com.damir.popravljanje.repository.EntitetiRepository;
import com.damir.popravljanje.repository.search.EntitetiSearchRepository;

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
 * Integration tests for the {@link EntitetiResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EntitetiResourceIT {

    private static final String DEFAULT_IME_ENTITETA = "AAAAAAAAAA";
    private static final String UPDATED_IME_ENTITETA = "BBBBBBBBBB";

    @Autowired
    private EntitetiRepository entitetiRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.EntitetiSearchRepositoryMockConfiguration
     */
    @Autowired
    private EntitetiSearchRepository mockEntitetiSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntitetiMockMvc;

    private Entiteti entiteti;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entiteti createEntity(EntityManager em) {
        Entiteti entiteti = new Entiteti()
            .imeEntiteta(DEFAULT_IME_ENTITETA);
        return entiteti;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entiteti createUpdatedEntity(EntityManager em) {
        Entiteti entiteti = new Entiteti()
            .imeEntiteta(UPDATED_IME_ENTITETA);
        return entiteti;
    }

    @BeforeEach
    public void initTest() {
        entiteti = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntiteti() throws Exception {
        int databaseSizeBeforeCreate = entitetiRepository.findAll().size();
        // Create the Entiteti
        restEntitetiMockMvc.perform(post("/api/entitetis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entiteti)))
            .andExpect(status().isCreated());

        // Validate the Entiteti in the database
        List<Entiteti> entitetiList = entitetiRepository.findAll();
        assertThat(entitetiList).hasSize(databaseSizeBeforeCreate + 1);
        Entiteti testEntiteti = entitetiList.get(entitetiList.size() - 1);
        assertThat(testEntiteti.getImeEntiteta()).isEqualTo(DEFAULT_IME_ENTITETA);

        // Validate the Entiteti in Elasticsearch
        verify(mockEntitetiSearchRepository, times(1)).save(testEntiteti);
    }

    @Test
    @Transactional
    public void createEntitetiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entitetiRepository.findAll().size();

        // Create the Entiteti with an existing ID
        entiteti.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntitetiMockMvc.perform(post("/api/entitetis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entiteti)))
            .andExpect(status().isBadRequest());

        // Validate the Entiteti in the database
        List<Entiteti> entitetiList = entitetiRepository.findAll();
        assertThat(entitetiList).hasSize(databaseSizeBeforeCreate);

        // Validate the Entiteti in Elasticsearch
        verify(mockEntitetiSearchRepository, times(0)).save(entiteti);
    }


    @Test
    @Transactional
    public void getAllEntitetis() throws Exception {
        // Initialize the database
        entitetiRepository.saveAndFlush(entiteti);

        // Get all the entitetiList
        restEntitetiMockMvc.perform(get("/api/entitetis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entiteti.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeEntiteta").value(hasItem(DEFAULT_IME_ENTITETA)));
    }
    
    @Test
    @Transactional
    public void getEntiteti() throws Exception {
        // Initialize the database
        entitetiRepository.saveAndFlush(entiteti);

        // Get the entiteti
        restEntitetiMockMvc.perform(get("/api/entitetis/{id}", entiteti.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entiteti.getId().intValue()))
            .andExpect(jsonPath("$.imeEntiteta").value(DEFAULT_IME_ENTITETA));
    }
    @Test
    @Transactional
    public void getNonExistingEntiteti() throws Exception {
        // Get the entiteti
        restEntitetiMockMvc.perform(get("/api/entitetis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntiteti() throws Exception {
        // Initialize the database
        entitetiRepository.saveAndFlush(entiteti);

        int databaseSizeBeforeUpdate = entitetiRepository.findAll().size();

        // Update the entiteti
        Entiteti updatedEntiteti = entitetiRepository.findById(entiteti.getId()).get();
        // Disconnect from session so that the updates on updatedEntiteti are not directly saved in db
        em.detach(updatedEntiteti);
        updatedEntiteti
            .imeEntiteta(UPDATED_IME_ENTITETA);

        restEntitetiMockMvc.perform(put("/api/entitetis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntiteti)))
            .andExpect(status().isOk());

        // Validate the Entiteti in the database
        List<Entiteti> entitetiList = entitetiRepository.findAll();
        assertThat(entitetiList).hasSize(databaseSizeBeforeUpdate);
        Entiteti testEntiteti = entitetiList.get(entitetiList.size() - 1);
        assertThat(testEntiteti.getImeEntiteta()).isEqualTo(UPDATED_IME_ENTITETA);

        // Validate the Entiteti in Elasticsearch
        verify(mockEntitetiSearchRepository, times(1)).save(testEntiteti);
    }

    @Test
    @Transactional
    public void updateNonExistingEntiteti() throws Exception {
        int databaseSizeBeforeUpdate = entitetiRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntitetiMockMvc.perform(put("/api/entitetis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entiteti)))
            .andExpect(status().isBadRequest());

        // Validate the Entiteti in the database
        List<Entiteti> entitetiList = entitetiRepository.findAll();
        assertThat(entitetiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Entiteti in Elasticsearch
        verify(mockEntitetiSearchRepository, times(0)).save(entiteti);
    }

    @Test
    @Transactional
    public void deleteEntiteti() throws Exception {
        // Initialize the database
        entitetiRepository.saveAndFlush(entiteti);

        int databaseSizeBeforeDelete = entitetiRepository.findAll().size();

        // Delete the entiteti
        restEntitetiMockMvc.perform(delete("/api/entitetis/{id}", entiteti.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entiteti> entitetiList = entitetiRepository.findAll();
        assertThat(entitetiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Entiteti in Elasticsearch
        verify(mockEntitetiSearchRepository, times(1)).deleteById(entiteti.getId());
    }

    @Test
    @Transactional
    public void searchEntiteti() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        entitetiRepository.saveAndFlush(entiteti);
        when(mockEntitetiSearchRepository.search(queryStringQuery("id:" + entiteti.getId())))
            .thenReturn(Collections.singletonList(entiteti));

        // Search the entiteti
        restEntitetiMockMvc.perform(get("/api/_search/entitetis?query=id:" + entiteti.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entiteti.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeEntiteta").value(hasItem(DEFAULT_IME_ENTITETA)));
    }
}
