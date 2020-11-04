package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.Artikl;
import com.damir.popravljanje.repository.ArtiklRepository;
import com.damir.popravljanje.repository.search.ArtiklSearchRepository;

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
 * Integration tests for the {@link ArtiklResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArtiklResourceIT {

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final String DEFAULT_KRATKI_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_KRATKI_OPIS = "BBBBBBBBBB";

    private static final String DEFAULT_DETALJNI_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_DETALJNI_OPIS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAJSTOR = false;
    private static final Boolean UPDATED_MAJSTOR = true;

    private static final Boolean DEFAULT_POSTOJI = false;
    private static final Boolean UPDATED_POSTOJI = true;

    @Autowired
    private ArtiklRepository artiklRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.ArtiklSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArtiklSearchRepository mockArtiklSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtiklMockMvc;

    private Artikl artikl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artikl createEntity(EntityManager em) {
        Artikl artikl = new Artikl()
            .ime(DEFAULT_IME)
            .kratkiOpis(DEFAULT_KRATKI_OPIS)
            .detaljniOpis(DEFAULT_DETALJNI_OPIS)
            .majstor(DEFAULT_MAJSTOR)
            .postoji(DEFAULT_POSTOJI);
        return artikl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artikl createUpdatedEntity(EntityManager em) {
        Artikl artikl = new Artikl()
            .ime(UPDATED_IME)
            .kratkiOpis(UPDATED_KRATKI_OPIS)
            .detaljniOpis(UPDATED_DETALJNI_OPIS)
            .majstor(UPDATED_MAJSTOR)
            .postoji(UPDATED_POSTOJI);
        return artikl;
    }

    @BeforeEach
    public void initTest() {
        artikl = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtikl() throws Exception {
        int databaseSizeBeforeCreate = artiklRepository.findAll().size();
        // Create the Artikl
        restArtiklMockMvc.perform(post("/api/artikls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artikl)))
            .andExpect(status().isCreated());

        // Validate the Artikl in the database
        List<Artikl> artiklList = artiklRepository.findAll();
        assertThat(artiklList).hasSize(databaseSizeBeforeCreate + 1);
        Artikl testArtikl = artiklList.get(artiklList.size() - 1);
        assertThat(testArtikl.getIme()).isEqualTo(DEFAULT_IME);
        assertThat(testArtikl.getKratkiOpis()).isEqualTo(DEFAULT_KRATKI_OPIS);
        assertThat(testArtikl.getDetaljniOpis()).isEqualTo(DEFAULT_DETALJNI_OPIS);
        assertThat(testArtikl.isMajstor()).isEqualTo(DEFAULT_MAJSTOR);
        assertThat(testArtikl.isPostoji()).isEqualTo(DEFAULT_POSTOJI);

        // Validate the Artikl in Elasticsearch
        verify(mockArtiklSearchRepository, times(1)).save(testArtikl);
    }

    @Test
    @Transactional
    public void createArtiklWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artiklRepository.findAll().size();

        // Create the Artikl with an existing ID
        artikl.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtiklMockMvc.perform(post("/api/artikls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artikl)))
            .andExpect(status().isBadRequest());

        // Validate the Artikl in the database
        List<Artikl> artiklList = artiklRepository.findAll();
        assertThat(artiklList).hasSize(databaseSizeBeforeCreate);

        // Validate the Artikl in Elasticsearch
        verify(mockArtiklSearchRepository, times(0)).save(artikl);
    }


    @Test
    @Transactional
    public void getAllArtikls() throws Exception {
        // Initialize the database
        artiklRepository.saveAndFlush(artikl);

        // Get all the artiklList
        restArtiklMockMvc.perform(get("/api/artikls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artikl.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].kratkiOpis").value(hasItem(DEFAULT_KRATKI_OPIS)))
            .andExpect(jsonPath("$.[*].detaljniOpis").value(hasItem(DEFAULT_DETALJNI_OPIS)))
            .andExpect(jsonPath("$.[*].majstor").value(hasItem(DEFAULT_MAJSTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getArtikl() throws Exception {
        // Initialize the database
        artiklRepository.saveAndFlush(artikl);

        // Get the artikl
        restArtiklMockMvc.perform(get("/api/artikls/{id}", artikl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artikl.getId().intValue()))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.kratkiOpis").value(DEFAULT_KRATKI_OPIS))
            .andExpect(jsonPath("$.detaljniOpis").value(DEFAULT_DETALJNI_OPIS))
            .andExpect(jsonPath("$.majstor").value(DEFAULT_MAJSTOR.booleanValue()))
            .andExpect(jsonPath("$.postoji").value(DEFAULT_POSTOJI.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingArtikl() throws Exception {
        // Get the artikl
        restArtiklMockMvc.perform(get("/api/artikls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtikl() throws Exception {
        // Initialize the database
        artiklRepository.saveAndFlush(artikl);

        int databaseSizeBeforeUpdate = artiklRepository.findAll().size();

        // Update the artikl
        Artikl updatedArtikl = artiklRepository.findById(artikl.getId()).get();
        // Disconnect from session so that the updates on updatedArtikl are not directly saved in db
        em.detach(updatedArtikl);
        updatedArtikl
            .ime(UPDATED_IME)
            .kratkiOpis(UPDATED_KRATKI_OPIS)
            .detaljniOpis(UPDATED_DETALJNI_OPIS)
            .majstor(UPDATED_MAJSTOR)
            .postoji(UPDATED_POSTOJI);

        restArtiklMockMvc.perform(put("/api/artikls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtikl)))
            .andExpect(status().isOk());

        // Validate the Artikl in the database
        List<Artikl> artiklList = artiklRepository.findAll();
        assertThat(artiklList).hasSize(databaseSizeBeforeUpdate);
        Artikl testArtikl = artiklList.get(artiklList.size() - 1);
        assertThat(testArtikl.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testArtikl.getKratkiOpis()).isEqualTo(UPDATED_KRATKI_OPIS);
        assertThat(testArtikl.getDetaljniOpis()).isEqualTo(UPDATED_DETALJNI_OPIS);
        assertThat(testArtikl.isMajstor()).isEqualTo(UPDATED_MAJSTOR);
        assertThat(testArtikl.isPostoji()).isEqualTo(UPDATED_POSTOJI);

        // Validate the Artikl in Elasticsearch
        verify(mockArtiklSearchRepository, times(1)).save(testArtikl);
    }

    @Test
    @Transactional
    public void updateNonExistingArtikl() throws Exception {
        int databaseSizeBeforeUpdate = artiklRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtiklMockMvc.perform(put("/api/artikls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artikl)))
            .andExpect(status().isBadRequest());

        // Validate the Artikl in the database
        List<Artikl> artiklList = artiklRepository.findAll();
        assertThat(artiklList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Artikl in Elasticsearch
        verify(mockArtiklSearchRepository, times(0)).save(artikl);
    }

    @Test
    @Transactional
    public void deleteArtikl() throws Exception {
        // Initialize the database
        artiklRepository.saveAndFlush(artikl);

        int databaseSizeBeforeDelete = artiklRepository.findAll().size();

        // Delete the artikl
        restArtiklMockMvc.perform(delete("/api/artikls/{id}", artikl.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artikl> artiklList = artiklRepository.findAll();
        assertThat(artiklList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Artikl in Elasticsearch
        verify(mockArtiklSearchRepository, times(1)).deleteById(artikl.getId());
    }

    @Test
    @Transactional
    public void searchArtikl() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        artiklRepository.saveAndFlush(artikl);
        when(mockArtiklSearchRepository.search(queryStringQuery("id:" + artikl.getId())))
            .thenReturn(Collections.singletonList(artikl));

        // Search the artikl
        restArtiklMockMvc.perform(get("/api/_search/artikls?query=id:" + artikl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artikl.getId().intValue())))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].kratkiOpis").value(hasItem(DEFAULT_KRATKI_OPIS)))
            .andExpect(jsonPath("$.[*].detaljniOpis").value(hasItem(DEFAULT_DETALJNI_OPIS)))
            .andExpect(jsonPath("$.[*].majstor").value(hasItem(DEFAULT_MAJSTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())));
    }
}
