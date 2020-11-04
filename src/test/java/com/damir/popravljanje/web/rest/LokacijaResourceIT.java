package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Lokacija;
import com.damir.popravljanje.repository.LokacijaRepository;
import com.damir.popravljanje.repository.search.LokacijaSearchRepository;

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
 * Integration tests for the {@link LokacijaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LokacijaResourceIT {

    private static final String DEFAULT_ADRESA = "AAAAAAAAAA";
    private static final String UPDATED_ADRESA = "BBBBBBBBBB";

    private static final String DEFAULT_POSTANSKI_BROJ = "AAAAAAAAAA";
    private static final String UPDATED_POSTANSKI_BROJ = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD = "AAAAAAAAAA";
    private static final String UPDATED_GRAD = "BBBBBBBBBB";

    @Autowired
    private LokacijaRepository lokacijaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.LokacijaSearchRepositoryMockConfiguration
     */
    @Autowired
    private LokacijaSearchRepository mockLokacijaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLokacijaMockMvc;

    private Lokacija lokacija;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lokacija createEntity(EntityManager em) {
        Lokacija lokacija = new Lokacija()
            .adresa(DEFAULT_ADRESA)
            .postanskiBroj(DEFAULT_POSTANSKI_BROJ)
            .grad(DEFAULT_GRAD);
        return lokacija;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lokacija createUpdatedEntity(EntityManager em) {
        Lokacija lokacija = new Lokacija()
            .adresa(UPDATED_ADRESA)
            .postanskiBroj(UPDATED_POSTANSKI_BROJ)
            .grad(UPDATED_GRAD);
        return lokacija;
    }

    @BeforeEach
    public void initTest() {
        lokacija = createEntity(em);
    }

    @Test
    @Transactional
    public void createLokacija() throws Exception {
        int databaseSizeBeforeCreate = lokacijaRepository.findAll().size();
        // Create the Lokacija
        restLokacijaMockMvc.perform(post("/api/lokacijas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lokacija)))
            .andExpect(status().isCreated());

        // Validate the Lokacija in the database
        List<Lokacija> lokacijaList = lokacijaRepository.findAll();
        assertThat(lokacijaList).hasSize(databaseSizeBeforeCreate + 1);
        Lokacija testLokacija = lokacijaList.get(lokacijaList.size() - 1);
        assertThat(testLokacija.getAdresa()).isEqualTo(DEFAULT_ADRESA);
        assertThat(testLokacija.getPostanskiBroj()).isEqualTo(DEFAULT_POSTANSKI_BROJ);
        assertThat(testLokacija.getGrad()).isEqualTo(DEFAULT_GRAD);

        // Validate the Lokacija in Elasticsearch
        verify(mockLokacijaSearchRepository, times(1)).save(testLokacija);
    }

    @Test
    @Transactional
    public void createLokacijaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lokacijaRepository.findAll().size();

        // Create the Lokacija with an existing ID
        lokacija.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLokacijaMockMvc.perform(post("/api/lokacijas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lokacija)))
            .andExpect(status().isBadRequest());

        // Validate the Lokacija in the database
        List<Lokacija> lokacijaList = lokacijaRepository.findAll();
        assertThat(lokacijaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lokacija in Elasticsearch
        verify(mockLokacijaSearchRepository, times(0)).save(lokacija);
    }


    @Test
    @Transactional
    public void getAllLokacijas() throws Exception {
        // Initialize the database
        lokacijaRepository.saveAndFlush(lokacija);

        // Get all the lokacijaList
        restLokacijaMockMvc.perform(get("/api/lokacijas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lokacija.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresa").value(hasItem(DEFAULT_ADRESA)))
            .andExpect(jsonPath("$.[*].postanskiBroj").value(hasItem(DEFAULT_POSTANSKI_BROJ)))
            .andExpect(jsonPath("$.[*].grad").value(hasItem(DEFAULT_GRAD)));
    }
    
    @Test
    @Transactional
    public void getLokacija() throws Exception {
        // Initialize the database
        lokacijaRepository.saveAndFlush(lokacija);

        // Get the lokacija
        restLokacijaMockMvc.perform(get("/api/lokacijas/{id}", lokacija.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lokacija.getId().intValue()))
            .andExpect(jsonPath("$.adresa").value(DEFAULT_ADRESA))
            .andExpect(jsonPath("$.postanskiBroj").value(DEFAULT_POSTANSKI_BROJ))
            .andExpect(jsonPath("$.grad").value(DEFAULT_GRAD));
    }
    @Test
    @Transactional
    public void getNonExistingLokacija() throws Exception {
        // Get the lokacija
        restLokacijaMockMvc.perform(get("/api/lokacijas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLokacija() throws Exception {
        // Initialize the database
        lokacijaRepository.saveAndFlush(lokacija);

        int databaseSizeBeforeUpdate = lokacijaRepository.findAll().size();

        // Update the lokacija
        Lokacija updatedLokacija = lokacijaRepository.findById(lokacija.getId()).get();
        // Disconnect from session so that the updates on updatedLokacija are not directly saved in db
        em.detach(updatedLokacija);
        updatedLokacija
            .adresa(UPDATED_ADRESA)
            .postanskiBroj(UPDATED_POSTANSKI_BROJ)
            .grad(UPDATED_GRAD);

        restLokacijaMockMvc.perform(put("/api/lokacijas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLokacija)))
            .andExpect(status().isOk());

        // Validate the Lokacija in the database
        List<Lokacija> lokacijaList = lokacijaRepository.findAll();
        assertThat(lokacijaList).hasSize(databaseSizeBeforeUpdate);
        Lokacija testLokacija = lokacijaList.get(lokacijaList.size() - 1);
        assertThat(testLokacija.getAdresa()).isEqualTo(UPDATED_ADRESA);
        assertThat(testLokacija.getPostanskiBroj()).isEqualTo(UPDATED_POSTANSKI_BROJ);
        assertThat(testLokacija.getGrad()).isEqualTo(UPDATED_GRAD);

        // Validate the Lokacija in Elasticsearch
        verify(mockLokacijaSearchRepository, times(1)).save(testLokacija);
    }

    @Test
    @Transactional
    public void updateNonExistingLokacija() throws Exception {
        int databaseSizeBeforeUpdate = lokacijaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLokacijaMockMvc.perform(put("/api/lokacijas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lokacija)))
            .andExpect(status().isBadRequest());

        // Validate the Lokacija in the database
        List<Lokacija> lokacijaList = lokacijaRepository.findAll();
        assertThat(lokacijaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lokacija in Elasticsearch
        verify(mockLokacijaSearchRepository, times(0)).save(lokacija);
    }

    @Test
    @Transactional
    public void deleteLokacija() throws Exception {
        // Initialize the database
        lokacijaRepository.saveAndFlush(lokacija);

        int databaseSizeBeforeDelete = lokacijaRepository.findAll().size();

        // Delete the lokacija
        restLokacijaMockMvc.perform(delete("/api/lokacijas/{id}", lokacija.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lokacija> lokacijaList = lokacijaRepository.findAll();
        assertThat(lokacijaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lokacija in Elasticsearch
        verify(mockLokacijaSearchRepository, times(1)).deleteById(lokacija.getId());
    }

    @Test
    @Transactional
    public void searchLokacija() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        lokacijaRepository.saveAndFlush(lokacija);
        when(mockLokacijaSearchRepository.search(queryStringQuery("id:" + lokacija.getId())))
            .thenReturn(Collections.singletonList(lokacija));

        // Search the lokacija
        restLokacijaMockMvc.perform(get("/api/_search/lokacijas?query=id:" + lokacija.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lokacija.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresa").value(hasItem(DEFAULT_ADRESA)))
            .andExpect(jsonPath("$.[*].postanskiBroj").value(hasItem(DEFAULT_POSTANSKI_BROJ)))
            .andExpect(jsonPath("$.[*].grad").value(hasItem(DEFAULT_GRAD)));
    }
}
