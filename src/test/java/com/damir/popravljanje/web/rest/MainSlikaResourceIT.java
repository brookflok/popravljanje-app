package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.MainSlika;
import com.damir.popravljanje.repository.MainSlikaRepository;
import com.damir.popravljanje.repository.search.MainSlikaSearchRepository;

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
 * Integration tests for the {@link MainSlikaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MainSlikaResourceIT {

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MainSlikaRepository mainSlikaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.MainSlikaSearchRepositoryMockConfiguration
     */
    @Autowired
    private MainSlikaSearchRepository mockMainSlikaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMainSlikaMockMvc;

    private MainSlika mainSlika;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainSlika createEntity(EntityManager em) {
        MainSlika mainSlika = new MainSlika()
            .ime(DEFAULT_IME)
            .datum(DEFAULT_DATUM);
        return mainSlika;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainSlika createUpdatedEntity(EntityManager em) {
        MainSlika mainSlika = new MainSlika()
            .ime(UPDATED_IME)
            .datum(UPDATED_DATUM);
        return mainSlika;
    }

    @BeforeEach
    public void initTest() {
        mainSlika = createEntity(em);
    }

    @Test
    @Transactional
    public void createMainSlika() throws Exception {
        int databaseSizeBeforeCreate = mainSlikaRepository.findAll().size();
        // Create the MainSlika
        restMainSlikaMockMvc.perform(post("/api/main-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mainSlika)))
            .andExpect(status().isCreated());

        // Validate the MainSlika in the database
        List<MainSlika> mainSlikaList = mainSlikaRepository.findAll();
        assertThat(mainSlikaList).hasSize(databaseSizeBeforeCreate + 1);
        MainSlika testMainSlika = mainSlikaList.get(mainSlikaList.size() - 1);
        assertThat(testMainSlika.getIme()).isEqualTo(DEFAULT_IME);
        assertThat(testMainSlika.getDatum()).isEqualTo(DEFAULT_DATUM);

        // Validate the MainSlika in Elasticsearch
        verify(mockMainSlikaSearchRepository, times(1)).save(testMainSlika);
    }

    @Test
    @Transactional
    public void createMainSlikaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mainSlikaRepository.findAll().size();

        // Create the MainSlika with an existing ID
        mainSlika.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMainSlikaMockMvc.perform(post("/api/main-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mainSlika)))
            .andExpect(status().isBadRequest());

        // Validate the MainSlika in the database
        List<MainSlika> mainSlikaList = mainSlikaRepository.findAll();
        assertThat(mainSlikaList).hasSize(databaseSizeBeforeCreate);

        // Validate the MainSlika in Elasticsearch
        verify(mockMainSlikaSearchRepository, times(0)).save(mainSlika);
    }


    @Test
    @Transactional
    public void getAllMainSlikas() throws Exception {
        // Initialize the database
        mainSlikaRepository.saveAndFlush(mainSlika);

        // Get all the mainSlikaList
        restMainSlikaMockMvc.perform(get("/api/main-slikas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainSlika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }
    
    @Test
    @Transactional
    public void getMainSlika() throws Exception {
        // Initialize the database
        mainSlikaRepository.saveAndFlush(mainSlika);

        // Get the mainSlika
        restMainSlikaMockMvc.perform(get("/api/main-slikas/{id}", mainSlika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mainSlika.getId().intValue()))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMainSlika() throws Exception {
        // Get the mainSlika
        restMainSlikaMockMvc.perform(get("/api/main-slikas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMainSlika() throws Exception {
        // Initialize the database
        mainSlikaRepository.saveAndFlush(mainSlika);

        int databaseSizeBeforeUpdate = mainSlikaRepository.findAll().size();

        // Update the mainSlika
        MainSlika updatedMainSlika = mainSlikaRepository.findById(mainSlika.getId()).get();
        // Disconnect from session so that the updates on updatedMainSlika are not directly saved in db
        em.detach(updatedMainSlika);
        updatedMainSlika
            .ime(UPDATED_IME)
            .datum(UPDATED_DATUM);

        restMainSlikaMockMvc.perform(put("/api/main-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMainSlika)))
            .andExpect(status().isOk());

        // Validate the MainSlika in the database
        List<MainSlika> mainSlikaList = mainSlikaRepository.findAll();
        assertThat(mainSlikaList).hasSize(databaseSizeBeforeUpdate);
        MainSlika testMainSlika = mainSlikaList.get(mainSlikaList.size() - 1);
        assertThat(testMainSlika.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testMainSlika.getDatum()).isEqualTo(UPDATED_DATUM);

        // Validate the MainSlika in Elasticsearch
        verify(mockMainSlikaSearchRepository, times(1)).save(testMainSlika);
    }

    @Test
    @Transactional
    public void updateNonExistingMainSlika() throws Exception {
        int databaseSizeBeforeUpdate = mainSlikaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainSlikaMockMvc.perform(put("/api/main-slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mainSlika)))
            .andExpect(status().isBadRequest());

        // Validate the MainSlika in the database
        List<MainSlika> mainSlikaList = mainSlikaRepository.findAll();
        assertThat(mainSlikaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MainSlika in Elasticsearch
        verify(mockMainSlikaSearchRepository, times(0)).save(mainSlika);
    }

    @Test
    @Transactional
    public void deleteMainSlika() throws Exception {
        // Initialize the database
        mainSlikaRepository.saveAndFlush(mainSlika);

        int databaseSizeBeforeDelete = mainSlikaRepository.findAll().size();

        // Delete the mainSlika
        restMainSlikaMockMvc.perform(delete("/api/main-slikas/{id}", mainSlika.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MainSlika> mainSlikaList = mainSlikaRepository.findAll();
        assertThat(mainSlikaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MainSlika in Elasticsearch
        verify(mockMainSlikaSearchRepository, times(1)).deleteById(mainSlika.getId());
    }

    @Test
    @Transactional
    public void searchMainSlika() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mainSlikaRepository.saveAndFlush(mainSlika);
        when(mockMainSlikaSearchRepository.search(queryStringQuery("id:" + mainSlika.getId())))
            .thenReturn(Collections.singletonList(mainSlika));

        // Search the mainSlika
        restMainSlikaMockMvc.perform(get("/api/_search/main-slikas?query=id:" + mainSlika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainSlika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }
}
