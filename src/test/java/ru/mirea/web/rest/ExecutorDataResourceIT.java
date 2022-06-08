package ru.mirea.web.rest;

import ru.mirea.ProviderApp;
import ru.mirea.domain.ExecutorData;
import ru.mirea.repository.ExecutorDataRepository;
import ru.mirea.service.ExecutorDataService;
import ru.mirea.service.dto.ExecutorDataCriteria;
import ru.mirea.service.ExecutorDataQueryService;

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
 * Integration tests for the {@link ExecutorDataResource} REST controller.
 */
@SpringBootTest(classes = ProviderApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExecutorDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    @Autowired
    private ExecutorDataRepository executorDataRepository;

    @Autowired
    private ExecutorDataService executorDataService;

    @Autowired
    private ExecutorDataQueryService executorDataQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExecutorDataMockMvc;

    private ExecutorData executorData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutorData createEntity(EntityManager em) {
        ExecutorData executorData = new ExecutorData()
            .name(DEFAULT_NAME)
            .data(DEFAULT_DATA);
        return executorData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutorData createUpdatedEntity(EntityManager em) {
        ExecutorData executorData = new ExecutorData()
            .name(UPDATED_NAME)
            .data(UPDATED_DATA);
        return executorData;
    }

    @BeforeEach
    public void initTest() {
        executorData = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecutorData() throws Exception {
        int databaseSizeBeforeCreate = executorDataRepository.findAll().size();
        // Create the ExecutorData
        restExecutorDataMockMvc.perform(post("/api/executor-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(executorData)))
            .andExpect(status().isCreated());

        // Validate the ExecutorData in the database
        List<ExecutorData> executorDataList = executorDataRepository.findAll();
        assertThat(executorDataList).hasSize(databaseSizeBeforeCreate + 1);
        ExecutorData testExecutorData = executorDataList.get(executorDataList.size() - 1);
        assertThat(testExecutorData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExecutorData.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createExecutorDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = executorDataRepository.findAll().size();

        // Create the ExecutorData with an existing ID
        executorData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutorDataMockMvc.perform(post("/api/executor-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(executorData)))
            .andExpect(status().isBadRequest());

        // Validate the ExecutorData in the database
        List<ExecutorData> executorDataList = executorDataRepository.findAll();
        assertThat(executorDataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExecutorData() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList
        restExecutorDataMockMvc.perform(get("/api/executor-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));
    }
    
    @Test
    @Transactional
    public void getExecutorData() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get the executorData
        restExecutorDataMockMvc.perform(get("/api/executor-data/{id}", executorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(executorData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA));
    }


    @Test
    @Transactional
    public void getExecutorDataByIdFiltering() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        Long id = executorData.getId();

        defaultExecutorDataShouldBeFound("id.equals=" + id);
        defaultExecutorDataShouldNotBeFound("id.notEquals=" + id);

        defaultExecutorDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExecutorDataShouldNotBeFound("id.greaterThan=" + id);

        defaultExecutorDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExecutorDataShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExecutorDataByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name equals to DEFAULT_NAME
        defaultExecutorDataShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the executorDataList where name equals to UPDATED_NAME
        defaultExecutorDataShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name not equals to DEFAULT_NAME
        defaultExecutorDataShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the executorDataList where name not equals to UPDATED_NAME
        defaultExecutorDataShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByNameIsInShouldWork() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExecutorDataShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the executorDataList where name equals to UPDATED_NAME
        defaultExecutorDataShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name is not null
        defaultExecutorDataShouldBeFound("name.specified=true");

        // Get all the executorDataList where name is null
        defaultExecutorDataShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllExecutorDataByNameContainsSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name contains DEFAULT_NAME
        defaultExecutorDataShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the executorDataList where name contains UPDATED_NAME
        defaultExecutorDataShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByNameNotContainsSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where name does not contain DEFAULT_NAME
        defaultExecutorDataShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the executorDataList where name does not contain UPDATED_NAME
        defaultExecutorDataShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllExecutorDataByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data equals to DEFAULT_DATA
        defaultExecutorDataShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the executorDataList where data equals to UPDATED_DATA
        defaultExecutorDataShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data not equals to DEFAULT_DATA
        defaultExecutorDataShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the executorDataList where data not equals to UPDATED_DATA
        defaultExecutorDataShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByDataIsInShouldWork() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data in DEFAULT_DATA or UPDATED_DATA
        defaultExecutorDataShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the executorDataList where data equals to UPDATED_DATA
        defaultExecutorDataShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data is not null
        defaultExecutorDataShouldBeFound("data.specified=true");

        // Get all the executorDataList where data is null
        defaultExecutorDataShouldNotBeFound("data.specified=false");
    }
                @Test
    @Transactional
    public void getAllExecutorDataByDataContainsSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data contains DEFAULT_DATA
        defaultExecutorDataShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the executorDataList where data contains UPDATED_DATA
        defaultExecutorDataShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllExecutorDataByDataNotContainsSomething() throws Exception {
        // Initialize the database
        executorDataRepository.saveAndFlush(executorData);

        // Get all the executorDataList where data does not contain DEFAULT_DATA
        defaultExecutorDataShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the executorDataList where data does not contain UPDATED_DATA
        defaultExecutorDataShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExecutorDataShouldBeFound(String filter) throws Exception {
        restExecutorDataMockMvc.perform(get("/api/executor-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));

        // Check, that the count call also returns 1
        restExecutorDataMockMvc.perform(get("/api/executor-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExecutorDataShouldNotBeFound(String filter) throws Exception {
        restExecutorDataMockMvc.perform(get("/api/executor-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExecutorDataMockMvc.perform(get("/api/executor-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingExecutorData() throws Exception {
        // Get the executorData
        restExecutorDataMockMvc.perform(get("/api/executor-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecutorData() throws Exception {
        // Initialize the database
        executorDataService.save(executorData);

        int databaseSizeBeforeUpdate = executorDataRepository.findAll().size();

        // Update the executorData
        ExecutorData updatedExecutorData = executorDataRepository.findById(executorData.getId()).get();
        // Disconnect from session so that the updates on updatedExecutorData are not directly saved in db
        em.detach(updatedExecutorData);
        updatedExecutorData
            .name(UPDATED_NAME)
            .data(UPDATED_DATA);

        restExecutorDataMockMvc.perform(put("/api/executor-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExecutorData)))
            .andExpect(status().isOk());

        // Validate the ExecutorData in the database
        List<ExecutorData> executorDataList = executorDataRepository.findAll();
        assertThat(executorDataList).hasSize(databaseSizeBeforeUpdate);
        ExecutorData testExecutorData = executorDataList.get(executorDataList.size() - 1);
        assertThat(testExecutorData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExecutorData.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingExecutorData() throws Exception {
        int databaseSizeBeforeUpdate = executorDataRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutorDataMockMvc.perform(put("/api/executor-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(executorData)))
            .andExpect(status().isBadRequest());

        // Validate the ExecutorData in the database
        List<ExecutorData> executorDataList = executorDataRepository.findAll();
        assertThat(executorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExecutorData() throws Exception {
        // Initialize the database
        executorDataService.save(executorData);

        int databaseSizeBeforeDelete = executorDataRepository.findAll().size();

        // Delete the executorData
        restExecutorDataMockMvc.perform(delete("/api/executor-data/{id}", executorData.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExecutorData> executorDataList = executorDataRepository.findAll();
        assertThat(executorDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
