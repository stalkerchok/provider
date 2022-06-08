package ru.mirea.web.rest;

import ru.mirea.ProviderApp;
import ru.mirea.domain.ManagerData;
import ru.mirea.repository.ManagerDataRepository;
import ru.mirea.service.ManagerDataService;
import ru.mirea.service.dto.ManagerDataCriteria;
import ru.mirea.service.ManagerDataQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ManagerDataResource} REST controller.
 */
@SpringBootTest(classes = ProviderApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ManagerDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    @Autowired
    private ManagerDataRepository managerDataRepository;

    @Autowired
    private ManagerDataService managerDataService;

    @Autowired
    private ManagerDataQueryService managerDataQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManagerDataMockMvc;

    private ManagerData managerData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagerData createEntity(EntityManager em) {
        ManagerData managerData = new ManagerData()
            .name(DEFAULT_NAME)
            .data(DEFAULT_DATA);
        return managerData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagerData createUpdatedEntity(EntityManager em) {
        ManagerData managerData = new ManagerData()
            .name(UPDATED_NAME)
            .data(UPDATED_DATA);
        return managerData;
    }

    @BeforeEach
    public void initTest() {
        managerData = createEntity(em);
    }

    @Test
    @Transactional
    public void createManagerData() throws Exception {
        int databaseSizeBeforeCreate = managerDataRepository.findAll().size();
        // Create the ManagerData
        restManagerDataMockMvc.perform(post("/api/manager-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(managerData)))
            .andExpect(status().isCreated());

        // Validate the ManagerData in the database
        List<ManagerData> managerDataList = managerDataRepository.findAll();
        assertThat(managerDataList).hasSize(databaseSizeBeforeCreate + 1);
        ManagerData testManagerData = managerDataList.get(managerDataList.size() - 1);
        assertThat(testManagerData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManagerData.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createManagerDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = managerDataRepository.findAll().size();

        // Create the ManagerData with an existing ID
        managerData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagerDataMockMvc.perform(post("/api/manager-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(managerData)))
            .andExpect(status().isBadRequest());

        // Validate the ManagerData in the database
        List<ManagerData> managerDataList = managerDataRepository.findAll();
        assertThat(managerDataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllManagerData() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList
        restManagerDataMockMvc.perform(get("/api/manager-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managerData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));
    }
    
    @Test
    @Transactional
    public void getManagerData() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get the managerData
        restManagerDataMockMvc.perform(get("/api/manager-data/{id}", managerData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(managerData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA));
    }


    @Test
    @Transactional
    public void getManagerDataByIdFiltering() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        Long id = managerData.getId();

        defaultManagerDataShouldBeFound("id.equals=" + id);
        defaultManagerDataShouldNotBeFound("id.notEquals=" + id);

        defaultManagerDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManagerDataShouldNotBeFound("id.greaterThan=" + id);

        defaultManagerDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManagerDataShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllManagerDataByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name equals to DEFAULT_NAME
        defaultManagerDataShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the managerDataList where name equals to UPDATED_NAME
        defaultManagerDataShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManagerDataByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name not equals to DEFAULT_NAME
        defaultManagerDataShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the managerDataList where name not equals to UPDATED_NAME
        defaultManagerDataShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManagerDataByNameIsInShouldWork() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name in DEFAULT_NAME or UPDATED_NAME
        defaultManagerDataShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the managerDataList where name equals to UPDATED_NAME
        defaultManagerDataShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManagerDataByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name is not null
        defaultManagerDataShouldBeFound("name.specified=true");

        // Get all the managerDataList where name is null
        defaultManagerDataShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllManagerDataByNameContainsSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name contains DEFAULT_NAME
        defaultManagerDataShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the managerDataList where name contains UPDATED_NAME
        defaultManagerDataShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManagerDataByNameNotContainsSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where name does not contain DEFAULT_NAME
        defaultManagerDataShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the managerDataList where name does not contain UPDATED_NAME
        defaultManagerDataShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllManagerDataByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data equals to DEFAULT_DATA
        defaultManagerDataShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the managerDataList where data equals to UPDATED_DATA
        defaultManagerDataShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllManagerDataByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data not equals to DEFAULT_DATA
        defaultManagerDataShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the managerDataList where data not equals to UPDATED_DATA
        defaultManagerDataShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllManagerDataByDataIsInShouldWork() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data in DEFAULT_DATA or UPDATED_DATA
        defaultManagerDataShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the managerDataList where data equals to UPDATED_DATA
        defaultManagerDataShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllManagerDataByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data is not null
        defaultManagerDataShouldBeFound("data.specified=true");

        // Get all the managerDataList where data is null
        defaultManagerDataShouldNotBeFound("data.specified=false");
    }
                @Test
    @Transactional
    public void getAllManagerDataByDataContainsSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data contains DEFAULT_DATA
        defaultManagerDataShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the managerDataList where data contains UPDATED_DATA
        defaultManagerDataShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllManagerDataByDataNotContainsSomething() throws Exception {
        // Initialize the database
        managerDataRepository.saveAndFlush(managerData);

        // Get all the managerDataList where data does not contain DEFAULT_DATA
        defaultManagerDataShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the managerDataList where data does not contain UPDATED_DATA
        defaultManagerDataShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManagerDataShouldBeFound(String filter) throws Exception {
        restManagerDataMockMvc.perform(get("/api/manager-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managerData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));

        // Check, that the count call also returns 1
        restManagerDataMockMvc.perform(get("/api/manager-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManagerDataShouldNotBeFound(String filter) throws Exception {
        restManagerDataMockMvc.perform(get("/api/manager-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManagerDataMockMvc.perform(get("/api/manager-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingManagerData() throws Exception {
        // Get the managerData
        restManagerDataMockMvc.perform(get("/api/manager-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManagerData() throws Exception {
        // Initialize the database
        managerDataService.save(managerData);

        int databaseSizeBeforeUpdate = managerDataRepository.findAll().size();

        // Update the managerData
        ManagerData updatedManagerData = managerDataRepository.findById(managerData.getId()).get();
        // Disconnect from session so that the updates on updatedManagerData are not directly saved in db
        em.detach(updatedManagerData);
        updatedManagerData
            .name(UPDATED_NAME)
            .data(UPDATED_DATA);

        restManagerDataMockMvc.perform(put("/api/manager-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedManagerData)))
            .andExpect(status().isOk());

        // Validate the ManagerData in the database
        List<ManagerData> managerDataList = managerDataRepository.findAll();
        assertThat(managerDataList).hasSize(databaseSizeBeforeUpdate);
        ManagerData testManagerData = managerDataList.get(managerDataList.size() - 1);
        assertThat(testManagerData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManagerData.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingManagerData() throws Exception {
        int databaseSizeBeforeUpdate = managerDataRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagerDataMockMvc.perform(put("/api/manager-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(managerData)))
            .andExpect(status().isBadRequest());

        // Validate the ManagerData in the database
        List<ManagerData> managerDataList = managerDataRepository.findAll();
        assertThat(managerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteManagerData() throws Exception {
        // Initialize the database
        managerDataService.save(managerData);

        int databaseSizeBeforeDelete = managerDataRepository.findAll().size();

        // Delete the managerData
        restManagerDataMockMvc.perform(delete("/api/manager-data/{id}", managerData.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManagerData> managerDataList = managerDataRepository.findAll();
        assertThat(managerDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
