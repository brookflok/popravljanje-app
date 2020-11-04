package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Slika;
import com.damir.popravljanje.repository.SlikaRepository;
import com.damir.popravljanje.repository.search.SlikaSearchRepository;

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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link SlikaResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SlikaResourceIT {

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SLIKA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SLIKA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SLIKA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SLIKA_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_UPLOADED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SlikaRepository slikaRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.SlikaSearchRepositoryMockConfiguration
     */
    @Autowired
    private SlikaSearchRepository mockSlikaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlikaMockMvc;

    private Slika slika;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slika createEntity(EntityManager em) {
        Slika slika = new Slika()
            .ime(DEFAULT_IME)
            .slika(DEFAULT_SLIKA)
            .slikaContentType(DEFAULT_SLIKA_CONTENT_TYPE)
            .uploaded(DEFAULT_UPLOADED);
        return slika;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slika createUpdatedEntity(EntityManager em) {
        Slika slika = new Slika()
            .ime(UPDATED_IME)
            .slika(UPDATED_SLIKA)
            .slikaContentType(UPDATED_SLIKA_CONTENT_TYPE)
            .uploaded(UPDATED_UPLOADED);
        return slika;
    }

    @BeforeEach
    public void initTest() {
        slika = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlika() throws Exception {
        int databaseSizeBeforeCreate = slikaRepository.findAll().size();
        // Create the Slika
        restSlikaMockMvc.perform(post("/api/slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slika)))
            .andExpect(status().isCreated());

        // Validate the Slika in the database
        List<Slika> slikaList = slikaRepository.findAll();
        assertThat(slikaList).hasSize(databaseSizeBeforeCreate + 1);
        Slika testSlika = slikaList.get(slikaList.size() - 1);
        assertThat(testSlika.getIme()).isEqualTo(DEFAULT_IME);
        assertThat(testSlika.getSlika()).isEqualTo(DEFAULT_SLIKA);
        assertThat(testSlika.getSlikaContentType()).isEqualTo(DEFAULT_SLIKA_CONTENT_TYPE);
        assertThat(testSlika.getUploaded()).isEqualTo(DEFAULT_UPLOADED);

        // Validate the Slika in Elasticsearch
        verify(mockSlikaSearchRepository, times(1)).save(testSlika);
    }

    @Test
    @Transactional
    public void createSlikaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slikaRepository.findAll().size();

        // Create the Slika with an existing ID
        slika.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlikaMockMvc.perform(post("/api/slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slika)))
            .andExpect(status().isBadRequest());

        // Validate the Slika in the database
        List<Slika> slikaList = slikaRepository.findAll();
        assertThat(slikaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Slika in Elasticsearch
        verify(mockSlikaSearchRepository, times(0)).save(slika);
    }


    @Test
    @Transactional
    public void getAllSlikas() throws Exception {
        // Initialize the database
        slikaRepository.saveAndFlush(slika);

        // Get all the slikaList
        restSlikaMockMvc.perform(get("/api/slikas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].slikaContentType").value(hasItem(DEFAULT_SLIKA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].slika").value(hasItem(Base64Utils.encodeToString(DEFAULT_SLIKA))))
            .andExpect(jsonPath("$.[*].uploaded").value(hasItem(DEFAULT_UPLOADED.toString())));
    }
    
    @Test
    @Transactional
    public void getSlika() throws Exception {
        // Initialize the database
        slikaRepository.saveAndFlush(slika);

        // Get the slika
        restSlikaMockMvc.perform(get("/api/slikas/{id}", slika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slika.getId().intValue()))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.slikaContentType").value(DEFAULT_SLIKA_CONTENT_TYPE))
            .andExpect(jsonPath("$.slika").value(Base64Utils.encodeToString(DEFAULT_SLIKA)))
            .andExpect(jsonPath("$.uploaded").value(DEFAULT_UPLOADED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSlika() throws Exception {
        // Get the slika
        restSlikaMockMvc.perform(get("/api/slikas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlika() throws Exception {
        // Initialize the database
        slikaRepository.saveAndFlush(slika);

        int databaseSizeBeforeUpdate = slikaRepository.findAll().size();

        // Update the slika
        Slika updatedSlika = slikaRepository.findById(slika.getId()).get();
        // Disconnect from session so that the updates on updatedSlika are not directly saved in db
        em.detach(updatedSlika);
        updatedSlika
            .ime(UPDATED_IME)
            .slika(UPDATED_SLIKA)
            .slikaContentType(UPDATED_SLIKA_CONTENT_TYPE)
            .uploaded(UPDATED_UPLOADED);

        restSlikaMockMvc.perform(put("/api/slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlika)))
            .andExpect(status().isOk());

        // Validate the Slika in the database
        List<Slika> slikaList = slikaRepository.findAll();
        assertThat(slikaList).hasSize(databaseSizeBeforeUpdate);
        Slika testSlika = slikaList.get(slikaList.size() - 1);
        assertThat(testSlika.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testSlika.getSlika()).isEqualTo(UPDATED_SLIKA);
        assertThat(testSlika.getSlikaContentType()).isEqualTo(UPDATED_SLIKA_CONTENT_TYPE);
        assertThat(testSlika.getUploaded()).isEqualTo(UPDATED_UPLOADED);

        // Validate the Slika in Elasticsearch
        verify(mockSlikaSearchRepository, times(1)).save(testSlika);
    }

    @Test
    @Transactional
    public void updateNonExistingSlika() throws Exception {
        int databaseSizeBeforeUpdate = slikaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlikaMockMvc.perform(put("/api/slikas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slika)))
            .andExpect(status().isBadRequest());

        // Validate the Slika in the database
        List<Slika> slikaList = slikaRepository.findAll();
        assertThat(slikaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Slika in Elasticsearch
        verify(mockSlikaSearchRepository, times(0)).save(slika);
    }

    @Test
    @Transactional
    public void deleteSlika() throws Exception {
        // Initialize the database
        slikaRepository.saveAndFlush(slika);

        int databaseSizeBeforeDelete = slikaRepository.findAll().size();

        // Delete the slika
        restSlikaMockMvc.perform(delete("/api/slikas/{id}", slika.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Slika> slikaList = slikaRepository.findAll();
        assertThat(slikaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Slika in Elasticsearch
        verify(mockSlikaSearchRepository, times(1)).deleteById(slika.getId());
    }

    @Test
    @Transactional
    public void searchSlika() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        slikaRepository.saveAndFlush(slika);
        when(mockSlikaSearchRepository.search(queryStringQuery("id:" + slika.getId())))
            .thenReturn(Collections.singletonList(slika));

        // Search the slika
        restSlikaMockMvc.perform(get("/api/_search/slikas?query=id:" + slika.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slika.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].slikaContentType").value(hasItem(DEFAULT_SLIKA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].slika").value(hasItem(Base64Utils.encodeToString(DEFAULT_SLIKA))))
            .andExpect(jsonPath("$.[*].uploaded").value(hasItem(DEFAULT_UPLOADED.toString())));
    }
}
