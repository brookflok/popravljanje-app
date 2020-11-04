package com.damir.popravljanje.web.rest;

import com.damir.popravljanje.PopravljanjeApp;
import com.damir.popravljanje.domain.DodatniInfoUser;
import com.damir.popravljanje.repository.DodatniInfoUserRepository;
import com.damir.popravljanje.repository.search.DodatniInfoUserSearchRepository;

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
 * Integration tests for the {@link DodatniInfoUserResource} REST controller.
 */
@SpringBootTest(classes = PopravljanjeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DodatniInfoUserResourceIT {

    private static final String DEFAULT_KORISNICKOIME = "AAAAAAAAAA";
    private static final String UPDATED_KORISNICKOIME = "BBBBBBBBBB";

    private static final String DEFAULT_BROJ_TELEFONA = "AAAAAAAAAA";
    private static final String UPDATED_BROJ_TELEFONA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAJSTOR = false;
    private static final Boolean UPDATED_MAJSTOR = true;

    private static final Boolean DEFAULT_POSTOJI = false;
    private static final Boolean UPDATED_POSTOJI = true;

    private static final String DEFAULT_DETALJNI_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_DETALJNI_OPIS = "BBBBBBBBBB";

    @Autowired
    private DodatniInfoUserRepository dodatniInfoUserRepository;

    /**
     * This repository is mocked in the com.damir.popravljanje.repository.search test package.
     *
     * @see com.damir.popravljanje.repository.search.DodatniInfoUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private DodatniInfoUserSearchRepository mockDodatniInfoUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDodatniInfoUserMockMvc;

    private DodatniInfoUser dodatniInfoUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DodatniInfoUser createEntity(EntityManager em) {
        DodatniInfoUser dodatniInfoUser = new DodatniInfoUser()
            .korisnickoime(DEFAULT_KORISNICKOIME)
            .brojTelefona(DEFAULT_BROJ_TELEFONA)
            .majstor(DEFAULT_MAJSTOR)
            .postoji(DEFAULT_POSTOJI)
            .detaljniOpis(DEFAULT_DETALJNI_OPIS);
        return dodatniInfoUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DodatniInfoUser createUpdatedEntity(EntityManager em) {
        DodatniInfoUser dodatniInfoUser = new DodatniInfoUser()
            .korisnickoime(UPDATED_KORISNICKOIME)
            .brojTelefona(UPDATED_BROJ_TELEFONA)
            .majstor(UPDATED_MAJSTOR)
            .postoji(UPDATED_POSTOJI)
            .detaljniOpis(UPDATED_DETALJNI_OPIS);
        return dodatniInfoUser;
    }

    @BeforeEach
    public void initTest() {
        dodatniInfoUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createDodatniInfoUser() throws Exception {
        int databaseSizeBeforeCreate = dodatniInfoUserRepository.findAll().size();
        // Create the DodatniInfoUser
        restDodatniInfoUserMockMvc.perform(post("/api/dodatni-info-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dodatniInfoUser)))
            .andExpect(status().isCreated());

        // Validate the DodatniInfoUser in the database
        List<DodatniInfoUser> dodatniInfoUserList = dodatniInfoUserRepository.findAll();
        assertThat(dodatniInfoUserList).hasSize(databaseSizeBeforeCreate + 1);
        DodatniInfoUser testDodatniInfoUser = dodatniInfoUserList.get(dodatniInfoUserList.size() - 1);
        assertThat(testDodatniInfoUser.getKorisnickoime()).isEqualTo(DEFAULT_KORISNICKOIME);
        assertThat(testDodatniInfoUser.getBrojTelefona()).isEqualTo(DEFAULT_BROJ_TELEFONA);
        assertThat(testDodatniInfoUser.isMajstor()).isEqualTo(DEFAULT_MAJSTOR);
        assertThat(testDodatniInfoUser.isPostoji()).isEqualTo(DEFAULT_POSTOJI);
        assertThat(testDodatniInfoUser.getDetaljniOpis()).isEqualTo(DEFAULT_DETALJNI_OPIS);

        // Validate the DodatniInfoUser in Elasticsearch
        verify(mockDodatniInfoUserSearchRepository, times(1)).save(testDodatniInfoUser);
    }

    @Test
    @Transactional
    public void createDodatniInfoUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dodatniInfoUserRepository.findAll().size();

        // Create the DodatniInfoUser with an existing ID
        dodatniInfoUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDodatniInfoUserMockMvc.perform(post("/api/dodatni-info-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dodatniInfoUser)))
            .andExpect(status().isBadRequest());

        // Validate the DodatniInfoUser in the database
        List<DodatniInfoUser> dodatniInfoUserList = dodatniInfoUserRepository.findAll();
        assertThat(dodatniInfoUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the DodatniInfoUser in Elasticsearch
        verify(mockDodatniInfoUserSearchRepository, times(0)).save(dodatniInfoUser);
    }


    @Test
    @Transactional
    public void getAllDodatniInfoUsers() throws Exception {
        // Initialize the database
        dodatniInfoUserRepository.saveAndFlush(dodatniInfoUser);

        // Get all the dodatniInfoUserList
        restDodatniInfoUserMockMvc.perform(get("/api/dodatni-info-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dodatniInfoUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].korisnickoime").value(hasItem(DEFAULT_KORISNICKOIME)))
            .andExpect(jsonPath("$.[*].brojTelefona").value(hasItem(DEFAULT_BROJ_TELEFONA)))
            .andExpect(jsonPath("$.[*].majstor").value(hasItem(DEFAULT_MAJSTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())))
            .andExpect(jsonPath("$.[*].detaljniOpis").value(hasItem(DEFAULT_DETALJNI_OPIS)));
    }
    
    @Test
    @Transactional
    public void getDodatniInfoUser() throws Exception {
        // Initialize the database
        dodatniInfoUserRepository.saveAndFlush(dodatniInfoUser);

        // Get the dodatniInfoUser
        restDodatniInfoUserMockMvc.perform(get("/api/dodatni-info-users/{id}", dodatniInfoUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dodatniInfoUser.getId().intValue()))
            .andExpect(jsonPath("$.korisnickoime").value(DEFAULT_KORISNICKOIME))
            .andExpect(jsonPath("$.brojTelefona").value(DEFAULT_BROJ_TELEFONA))
            .andExpect(jsonPath("$.majstor").value(DEFAULT_MAJSTOR.booleanValue()))
            .andExpect(jsonPath("$.postoji").value(DEFAULT_POSTOJI.booleanValue()))
            .andExpect(jsonPath("$.detaljniOpis").value(DEFAULT_DETALJNI_OPIS));
    }
    @Test
    @Transactional
    public void getNonExistingDodatniInfoUser() throws Exception {
        // Get the dodatniInfoUser
        restDodatniInfoUserMockMvc.perform(get("/api/dodatni-info-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDodatniInfoUser() throws Exception {
        // Initialize the database
        dodatniInfoUserRepository.saveAndFlush(dodatniInfoUser);

        int databaseSizeBeforeUpdate = dodatniInfoUserRepository.findAll().size();

        // Update the dodatniInfoUser
        DodatniInfoUser updatedDodatniInfoUser = dodatniInfoUserRepository.findById(dodatniInfoUser.getId()).get();
        // Disconnect from session so that the updates on updatedDodatniInfoUser are not directly saved in db
        em.detach(updatedDodatniInfoUser);
        updatedDodatniInfoUser
            .korisnickoime(UPDATED_KORISNICKOIME)
            .brojTelefona(UPDATED_BROJ_TELEFONA)
            .majstor(UPDATED_MAJSTOR)
            .postoji(UPDATED_POSTOJI)
            .detaljniOpis(UPDATED_DETALJNI_OPIS);

        restDodatniInfoUserMockMvc.perform(put("/api/dodatni-info-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDodatniInfoUser)))
            .andExpect(status().isOk());

        // Validate the DodatniInfoUser in the database
        List<DodatniInfoUser> dodatniInfoUserList = dodatniInfoUserRepository.findAll();
        assertThat(dodatniInfoUserList).hasSize(databaseSizeBeforeUpdate);
        DodatniInfoUser testDodatniInfoUser = dodatniInfoUserList.get(dodatniInfoUserList.size() - 1);
        assertThat(testDodatniInfoUser.getKorisnickoime()).isEqualTo(UPDATED_KORISNICKOIME);
        assertThat(testDodatniInfoUser.getBrojTelefona()).isEqualTo(UPDATED_BROJ_TELEFONA);
        assertThat(testDodatniInfoUser.isMajstor()).isEqualTo(UPDATED_MAJSTOR);
        assertThat(testDodatniInfoUser.isPostoji()).isEqualTo(UPDATED_POSTOJI);
        assertThat(testDodatniInfoUser.getDetaljniOpis()).isEqualTo(UPDATED_DETALJNI_OPIS);

        // Validate the DodatniInfoUser in Elasticsearch
        verify(mockDodatniInfoUserSearchRepository, times(1)).save(testDodatniInfoUser);
    }

    @Test
    @Transactional
    public void updateNonExistingDodatniInfoUser() throws Exception {
        int databaseSizeBeforeUpdate = dodatniInfoUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDodatniInfoUserMockMvc.perform(put("/api/dodatni-info-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dodatniInfoUser)))
            .andExpect(status().isBadRequest());

        // Validate the DodatniInfoUser in the database
        List<DodatniInfoUser> dodatniInfoUserList = dodatniInfoUserRepository.findAll();
        assertThat(dodatniInfoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DodatniInfoUser in Elasticsearch
        verify(mockDodatniInfoUserSearchRepository, times(0)).save(dodatniInfoUser);
    }

    @Test
    @Transactional
    public void deleteDodatniInfoUser() throws Exception {
        // Initialize the database
        dodatniInfoUserRepository.saveAndFlush(dodatniInfoUser);

        int databaseSizeBeforeDelete = dodatniInfoUserRepository.findAll().size();

        // Delete the dodatniInfoUser
        restDodatniInfoUserMockMvc.perform(delete("/api/dodatni-info-users/{id}", dodatniInfoUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DodatniInfoUser> dodatniInfoUserList = dodatniInfoUserRepository.findAll();
        assertThat(dodatniInfoUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DodatniInfoUser in Elasticsearch
        verify(mockDodatniInfoUserSearchRepository, times(1)).deleteById(dodatniInfoUser.getId());
    }

    @Test
    @Transactional
    public void searchDodatniInfoUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dodatniInfoUserRepository.saveAndFlush(dodatniInfoUser);
        when(mockDodatniInfoUserSearchRepository.search(queryStringQuery("id:" + dodatniInfoUser.getId())))
            .thenReturn(Collections.singletonList(dodatniInfoUser));

        // Search the dodatniInfoUser
        restDodatniInfoUserMockMvc.perform(get("/api/_search/dodatni-info-users?query=id:" + dodatniInfoUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dodatniInfoUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].korisnickoime").value(hasItem(DEFAULT_KORISNICKOIME)))
            .andExpect(jsonPath("$.[*].brojTelefona").value(hasItem(DEFAULT_BROJ_TELEFONA)))
            .andExpect(jsonPath("$.[*].majstor").value(hasItem(DEFAULT_MAJSTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].postoji").value(hasItem(DEFAULT_POSTOJI.booleanValue())))
            .andExpect(jsonPath("$.[*].detaljniOpis").value(hasItem(DEFAULT_DETALJNI_OPIS)));
    }
}
