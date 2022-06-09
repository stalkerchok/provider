package ru.mirea.web.rest;

import ru.mirea.ProviderApp;
import ru.mirea.domain.TemporaryAccess;
import ru.mirea.repository.TemporaryAccessRepository;
import ru.mirea.service.TemporaryAccessService;
import ru.mirea.service.dto.TemporaryAccessCriteria;
import ru.mirea.service.TemporaryAccessQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.mirea.domain.enumeration.PermissionType;
import ru.mirea.domain.enumeration.EntityClass;
/**
 * Integration tests for the {@link TemporaryAccessResource} REST controller.
 */
@SpringBootTest(classes = ProviderApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TemporaryAccessResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final PermissionType DEFAULT_PERMISSION_TYPE = PermissionType.RO;
    private static final PermissionType UPDATED_PERMISSION_TYPE = PermissionType.RW;

    private static final EntityClass DEFAULT_ENTITY_CLASS = EntityClass.MANAGER_DATA;
    private static final EntityClass UPDATED_ENTITY_CLASS = EntityClass.EXECUTOR_DATA;

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;
    private static final Long SMALLER_ENTITY_ID = 1L - 1L;

    @Autowired
    private TemporaryAccessRepository temporaryAccessRepository;

    @Autowired
    private TemporaryAccessService temporaryAccessService;

    @Autowired
    private TemporaryAccessQueryService temporaryAccessQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemporaryAccessMockMvc;

    private TemporaryAccess temporaryAccess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemporaryAccess createEntity(EntityManager em) {
        TemporaryAccess temporaryAccess = new TemporaryAccess()
            .login(DEFAULT_LOGIN)
            .endDate(DEFAULT_END_DATE)
            .permissionType(DEFAULT_PERMISSION_TYPE)
            .entityClass(DEFAULT_ENTITY_CLASS)
            .entityId(DEFAULT_ENTITY_ID);
        return temporaryAccess;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemporaryAccess createUpdatedEntity(EntityManager em) {
        TemporaryAccess temporaryAccess = new TemporaryAccess()
            .login(UPDATED_LOGIN)
            .endDate(UPDATED_END_DATE)
            .permissionType(UPDATED_PERMISSION_TYPE)
            .entityClass(UPDATED_ENTITY_CLASS)
            .entityId(UPDATED_ENTITY_ID);
        return temporaryAccess;
    }

    @BeforeEach
    public void initTest() {
        temporaryAccess = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemporaryAccess() throws Exception {
        int databaseSizeBeforeCreate = temporaryAccessRepository.findAll().size();
        // Create the TemporaryAccess
        restTemporaryAccessMockMvc.perform(post("/api/temporary-accesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporaryAccess)))
            .andExpect(status().isCreated());

        // Validate the TemporaryAccess in the database
        List<TemporaryAccess> temporaryAccessList = temporaryAccessRepository.findAll();
        assertThat(temporaryAccessList).hasSize(databaseSizeBeforeCreate + 1);
        TemporaryAccess testTemporaryAccess = temporaryAccessList.get(temporaryAccessList.size() - 1);
        assertThat(testTemporaryAccess.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testTemporaryAccess.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTemporaryAccess.getPermissionType()).isEqualTo(DEFAULT_PERMISSION_TYPE);
        assertThat(testTemporaryAccess.getEntityClass()).isEqualTo(DEFAULT_ENTITY_CLASS);
        assertThat(testTemporaryAccess.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    public void createTemporaryAccessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = temporaryAccessRepository.findAll().size();

        // Create the TemporaryAccess with an existing ID
        temporaryAccess.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemporaryAccessMockMvc.perform(post("/api/temporary-accesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporaryAccess)))
            .andExpect(status().isBadRequest());

        // Validate the TemporaryAccess in the database
        List<TemporaryAccess> temporaryAccessList = temporaryAccessRepository.findAll();
        assertThat(temporaryAccessList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTemporaryAccesses() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temporaryAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].permissionType").value(hasItem(DEFAULT_PERMISSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityClass").value(hasItem(DEFAULT_ENTITY_CLASS.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getTemporaryAccess() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get the temporaryAccess
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses/{id}", temporaryAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temporaryAccess.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.permissionType").value(DEFAULT_PERMISSION_TYPE.toString()))
            .andExpect(jsonPath("$.entityClass").value(DEFAULT_ENTITY_CLASS.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()));
    }


    @Test
    @Transactional
    public void getTemporaryAccessesByIdFiltering() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        Long id = temporaryAccess.getId();

        defaultTemporaryAccessShouldBeFound("id.equals=" + id);
        defaultTemporaryAccessShouldNotBeFound("id.notEquals=" + id);

        defaultTemporaryAccessShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemporaryAccessShouldNotBeFound("id.greaterThan=" + id);

        defaultTemporaryAccessShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemporaryAccessShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login equals to DEFAULT_LOGIN
        defaultTemporaryAccessShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the temporaryAccessList where login equals to UPDATED_LOGIN
        defaultTemporaryAccessShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login not equals to DEFAULT_LOGIN
        defaultTemporaryAccessShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the temporaryAccessList where login not equals to UPDATED_LOGIN
        defaultTemporaryAccessShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultTemporaryAccessShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the temporaryAccessList where login equals to UPDATED_LOGIN
        defaultTemporaryAccessShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login is not null
        defaultTemporaryAccessShouldBeFound("login.specified=true");

        // Get all the temporaryAccessList where login is null
        defaultTemporaryAccessShouldNotBeFound("login.specified=false");
    }
                @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginContainsSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login contains DEFAULT_LOGIN
        defaultTemporaryAccessShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the temporaryAccessList where login contains UPDATED_LOGIN
        defaultTemporaryAccessShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where login does not contain DEFAULT_LOGIN
        defaultTemporaryAccessShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the temporaryAccessList where login does not contain UPDATED_LOGIN
        defaultTemporaryAccessShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }


    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate equals to DEFAULT_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate equals to UPDATED_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate not equals to DEFAULT_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate not equals to UPDATED_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the temporaryAccessList where endDate equals to UPDATED_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate is not null
        defaultTemporaryAccessShouldBeFound("endDate.specified=true");

        // Get all the temporaryAccessList where endDate is null
        defaultTemporaryAccessShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate is greater than or equal to UPDATED_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate is less than or equal to DEFAULT_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate is less than or equal to SMALLER_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate is less than DEFAULT_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate is less than UPDATED_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where endDate is greater than DEFAULT_END_DATE
        defaultTemporaryAccessShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the temporaryAccessList where endDate is greater than SMALLER_END_DATE
        defaultTemporaryAccessShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllTemporaryAccessesByPermissionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where permissionType equals to DEFAULT_PERMISSION_TYPE
        defaultTemporaryAccessShouldBeFound("permissionType.equals=" + DEFAULT_PERMISSION_TYPE);

        // Get all the temporaryAccessList where permissionType equals to UPDATED_PERMISSION_TYPE
        defaultTemporaryAccessShouldNotBeFound("permissionType.equals=" + UPDATED_PERMISSION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByPermissionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where permissionType not equals to DEFAULT_PERMISSION_TYPE
        defaultTemporaryAccessShouldNotBeFound("permissionType.notEquals=" + DEFAULT_PERMISSION_TYPE);

        // Get all the temporaryAccessList where permissionType not equals to UPDATED_PERMISSION_TYPE
        defaultTemporaryAccessShouldBeFound("permissionType.notEquals=" + UPDATED_PERMISSION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByPermissionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where permissionType in DEFAULT_PERMISSION_TYPE or UPDATED_PERMISSION_TYPE
        defaultTemporaryAccessShouldBeFound("permissionType.in=" + DEFAULT_PERMISSION_TYPE + "," + UPDATED_PERMISSION_TYPE);

        // Get all the temporaryAccessList where permissionType equals to UPDATED_PERMISSION_TYPE
        defaultTemporaryAccessShouldNotBeFound("permissionType.in=" + UPDATED_PERMISSION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByPermissionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where permissionType is not null
        defaultTemporaryAccessShouldBeFound("permissionType.specified=true");

        // Get all the temporaryAccessList where permissionType is null
        defaultTemporaryAccessShouldNotBeFound("permissionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityClassIsEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityClass equals to DEFAULT_ENTITY_CLASS
        defaultTemporaryAccessShouldBeFound("entityClass.equals=" + DEFAULT_ENTITY_CLASS);

        // Get all the temporaryAccessList where entityClass equals to UPDATED_ENTITY_CLASS
        defaultTemporaryAccessShouldNotBeFound("entityClass.equals=" + UPDATED_ENTITY_CLASS);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityClassIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityClass not equals to DEFAULT_ENTITY_CLASS
        defaultTemporaryAccessShouldNotBeFound("entityClass.notEquals=" + DEFAULT_ENTITY_CLASS);

        // Get all the temporaryAccessList where entityClass not equals to UPDATED_ENTITY_CLASS
        defaultTemporaryAccessShouldBeFound("entityClass.notEquals=" + UPDATED_ENTITY_CLASS);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityClassIsInShouldWork() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityClass in DEFAULT_ENTITY_CLASS or UPDATED_ENTITY_CLASS
        defaultTemporaryAccessShouldBeFound("entityClass.in=" + DEFAULT_ENTITY_CLASS + "," + UPDATED_ENTITY_CLASS);

        // Get all the temporaryAccessList where entityClass equals to UPDATED_ENTITY_CLASS
        defaultTemporaryAccessShouldNotBeFound("entityClass.in=" + UPDATED_ENTITY_CLASS);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityClass is not null
        defaultTemporaryAccessShouldBeFound("entityClass.specified=true");

        // Get all the temporaryAccessList where entityClass is null
        defaultTemporaryAccessShouldNotBeFound("entityClass.specified=false");
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId equals to DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.equals=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId equals to UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId not equals to DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.notEquals=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId not equals to UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.notEquals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId in DEFAULT_ENTITY_ID or UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID);

        // Get all the temporaryAccessList where entityId equals to UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId is not null
        defaultTemporaryAccessShouldBeFound("entityId.specified=true");

        // Get all the temporaryAccessList where entityId is null
        defaultTemporaryAccessShouldNotBeFound("entityId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId is greater than or equal to DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.greaterThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId is greater than or equal to UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.greaterThanOrEqual=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId is less than or equal to DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.lessThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId is less than or equal to SMALLER_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.lessThanOrEqual=" + SMALLER_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId is less than DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.lessThan=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId is less than UPDATED_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.lessThan=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllTemporaryAccessesByEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        temporaryAccessRepository.saveAndFlush(temporaryAccess);

        // Get all the temporaryAccessList where entityId is greater than DEFAULT_ENTITY_ID
        defaultTemporaryAccessShouldNotBeFound("entityId.greaterThan=" + DEFAULT_ENTITY_ID);

        // Get all the temporaryAccessList where entityId is greater than SMALLER_ENTITY_ID
        defaultTemporaryAccessShouldBeFound("entityId.greaterThan=" + SMALLER_ENTITY_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemporaryAccessShouldBeFound(String filter) throws Exception {
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temporaryAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].permissionType").value(hasItem(DEFAULT_PERMISSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityClass").value(hasItem(DEFAULT_ENTITY_CLASS.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));

        // Check, that the count call also returns 1
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemporaryAccessShouldNotBeFound(String filter) throws Exception {
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTemporaryAccess() throws Exception {
        // Get the temporaryAccess
        restTemporaryAccessMockMvc.perform(get("/api/temporary-accesses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemporaryAccess() throws Exception {
        // Initialize the database
        temporaryAccessService.save(temporaryAccess);

        int databaseSizeBeforeUpdate = temporaryAccessRepository.findAll().size();

        // Update the temporaryAccess
        TemporaryAccess updatedTemporaryAccess = temporaryAccessRepository.findById(temporaryAccess.getId()).get();
        // Disconnect from session so that the updates on updatedTemporaryAccess are not directly saved in db
        em.detach(updatedTemporaryAccess);
        updatedTemporaryAccess
            .login(UPDATED_LOGIN)
            .endDate(UPDATED_END_DATE)
            .permissionType(UPDATED_PERMISSION_TYPE)
            .entityClass(UPDATED_ENTITY_CLASS)
            .entityId(UPDATED_ENTITY_ID);

        restTemporaryAccessMockMvc.perform(put("/api/temporary-accesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemporaryAccess)))
            .andExpect(status().isOk());

        // Validate the TemporaryAccess in the database
        List<TemporaryAccess> temporaryAccessList = temporaryAccessRepository.findAll();
        assertThat(temporaryAccessList).hasSize(databaseSizeBeforeUpdate);
        TemporaryAccess testTemporaryAccess = temporaryAccessList.get(temporaryAccessList.size() - 1);
        assertThat(testTemporaryAccess.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testTemporaryAccess.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTemporaryAccess.getPermissionType()).isEqualTo(UPDATED_PERMISSION_TYPE);
        assertThat(testTemporaryAccess.getEntityClass()).isEqualTo(UPDATED_ENTITY_CLASS);
        assertThat(testTemporaryAccess.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTemporaryAccess() throws Exception {
        int databaseSizeBeforeUpdate = temporaryAccessRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemporaryAccessMockMvc.perform(put("/api/temporary-accesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporaryAccess)))
            .andExpect(status().isBadRequest());

        // Validate the TemporaryAccess in the database
        List<TemporaryAccess> temporaryAccessList = temporaryAccessRepository.findAll();
        assertThat(temporaryAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTemporaryAccess() throws Exception {
        // Initialize the database
        temporaryAccessService.save(temporaryAccess);

        int databaseSizeBeforeDelete = temporaryAccessRepository.findAll().size();

        // Delete the temporaryAccess
        restTemporaryAccessMockMvc.perform(delete("/api/temporary-accesses/{id}", temporaryAccess.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemporaryAccess> temporaryAccessList = temporaryAccessRepository.findAll();
        assertThat(temporaryAccessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
