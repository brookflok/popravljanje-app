package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Ucesnici;
import com.damir.popravljanje.repository.UcesniciRepository;
import com.damir.popravljanje.repository.search.UcesniciSearchRepository;

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
 * Integration tests for the {@link UcesniciResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UcesniciResourceIT {

    @Autowired
    private UcesniciRepository ucesniciRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.UcesniciSearchRepositoryMockConfiguration
     */
    @Autowired
    private UcesniciSearchRepository mockUcesniciSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUcesniciMockMvc;

    private Ucesnici ucesnici;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ucesnici createEntity(EntityManager em) {
        Ucesnici ucesnici = new Ucesnici();
        return ucesnici;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ucesnici createUpdatedEntity(EntityManager em) {
        Ucesnici ucesnici = new Ucesnici();
        return ucesnici;
    }

    @BeforeEach
    public void initTest() {
        ucesnici = createEntity(em);
    }

    @Test
    @Transactional
    public void createUcesnici() throws Exception {
        int databaseSizeBeforeCreate = ucesniciRepository.findAll().size();
        // Create the Ucesnici
        restUcesniciMockMvc.perform(post("/api/ucesnicis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ucesnici)))
            .andExpect(status().isCreated());

        // Validate the Ucesnici in the database
        List<Ucesnici> ucesniciList = ucesniciRepository.findAll();
        assertThat(ucesniciList).hasSize(databaseSizeBeforeCreate + 1);
        Ucesnici testUcesnici = ucesniciList.get(ucesniciList.size() - 1);

        // Validate the Ucesnici in Elasticsearch
        verify(mockUcesniciSearchRepository, times(1)).save(testUcesnici);
    }

    @Test
    @Transactional
    public void createUcesniciWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ucesniciRepository.findAll().size();

        // Create the Ucesnici with an existing ID
        ucesnici.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUcesniciMockMvc.perform(post("/api/ucesnicis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ucesnici)))
            .andExpect(status().isBadRequest());

        // Validate the Ucesnici in the database
        List<Ucesnici> ucesniciList = ucesniciRepository.findAll();
        assertThat(ucesniciList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ucesnici in Elasticsearch
        verify(mockUcesniciSearchRepository, times(0)).save(ucesnici);
    }


    @Test
    @Transactional
    public void getAllUcesnicis() throws Exception {
        // Initialize the database
        ucesniciRepository.saveAndFlush(ucesnici);

        // Get all the ucesniciList
        restUcesniciMockMvc.perform(get("/api/ucesnicis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ucesnici.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getUcesnici() throws Exception {
        // Initialize the database
        ucesniciRepository.saveAndFlush(ucesnici);

        // Get the ucesnici
        restUcesniciMockMvc.perform(get("/api/ucesnicis/{id}", ucesnici.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ucesnici.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingUcesnici() throws Exception {
        // Get the ucesnici
        restUcesniciMockMvc.perform(get("/api/ucesnicis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUcesnici() throws Exception {
        // Initialize the database
        ucesniciRepository.saveAndFlush(ucesnici);

        int databaseSizeBeforeUpdate = ucesniciRepository.findAll().size();

        // Update the ucesnici
        Ucesnici updatedUcesnici = ucesniciRepository.findById(ucesnici.getId()).get();
        // Disconnect from session so that the updates on updatedUcesnici are not directly saved in db
        em.detach(updatedUcesnici);

        restUcesniciMockMvc.perform(put("/api/ucesnicis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUcesnici)))
            .andExpect(status().isOk());

        // Validate the Ucesnici in the database
        List<Ucesnici> ucesniciList = ucesniciRepository.findAll();
        assertThat(ucesniciList).hasSize(databaseSizeBeforeUpdate);
        Ucesnici testUcesnici = ucesniciList.get(ucesniciList.size() - 1);

        // Validate the Ucesnici in Elasticsearch
        verify(mockUcesniciSearchRepository, times(1)).save(testUcesnici);
    }

    @Test
    @Transactional
    public void updateNonExistingUcesnici() throws Exception {
        int databaseSizeBeforeUpdate = ucesniciRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUcesniciMockMvc.perform(put("/api/ucesnicis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ucesnici)))
            .andExpect(status().isBadRequest());

        // Validate the Ucesnici in the database
        List<Ucesnici> ucesniciList = ucesniciRepository.findAll();
        assertThat(ucesniciList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ucesnici in Elasticsearch
        verify(mockUcesniciSearchRepository, times(0)).save(ucesnici);
    }

    @Test
    @Transactional
    public void deleteUcesnici() throws Exception {
        // Initialize the database
        ucesniciRepository.saveAndFlush(ucesnici);

        int databaseSizeBeforeDelete = ucesniciRepository.findAll().size();

        // Delete the ucesnici
        restUcesniciMockMvc.perform(delete("/api/ucesnicis/{id}", ucesnici.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ucesnici> ucesniciList = ucesniciRepository.findAll();
        assertThat(ucesniciList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ucesnici in Elasticsearch
        verify(mockUcesniciSearchRepository, times(1)).deleteById(ucesnici.getId());
    }

    @Test
    @Transactional
    public void searchUcesnici() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ucesniciRepository.saveAndFlush(ucesnici);
        when(mockUcesniciSearchRepository.search(queryStringQuery("id:" + ucesnici.getId())))
            .thenReturn(Collections.singletonList(ucesnici));

        // Search the ucesnici
        restUcesniciMockMvc.perform(get("/api/_search/ucesnicis?query=id:" + ucesnici.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ucesnici.getId().intValue())));
    }
}
