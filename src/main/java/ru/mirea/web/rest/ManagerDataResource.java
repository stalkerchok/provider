package ru.mirea.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.mirea.domain.ManagerData;
import ru.mirea.service.ManagerDataService;
import ru.mirea.service.PermissionService;
import ru.mirea.web.rest.errors.BadRequestAlertException;
import ru.mirea.service.dto.ManagerDataCriteria;
import ru.mirea.service.ManagerDataQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ru.mirea.domain.ManagerData}.
 */
@RestController
@RequestMapping("/api")
public class ManagerDataResource {

    private final Logger log = LoggerFactory.getLogger(ManagerDataResource.class);

    private static final String ENTITY_NAME = "managerData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManagerDataService managerDataService;

    private final ManagerDataQueryService managerDataQueryService;

    private final PermissionService permissionService;

    public ManagerDataResource(ManagerDataService managerDataService,
                               ManagerDataQueryService managerDataQueryService,
                               PermissionService permissionService) {
        this.managerDataService = managerDataService;
        this.managerDataQueryService = managerDataQueryService;
        this.permissionService = permissionService;
    }

    /**
     * {@code POST  /manager-data} : Create a new managerData.
     *
     * @param managerData the managerData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new managerData, or with status {@code 400 (Bad Request)} if the managerData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@permissionService.hasManagerDataRwAccess(#managerData)")
    @PostMapping("/manager-data")
    public ResponseEntity<ManagerData> createManagerData(@RequestBody ManagerData managerData) throws URISyntaxException {
        log.debug("REST request to save ManagerData : {}", managerData);
        if (managerData.getId() != null) {
            throw new BadRequestAlertException("A new managerData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManagerData result = managerDataService.save(managerData);
        return ResponseEntity.created(new URI("/api/manager-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manager-data} : Updates an existing managerData.
     *
     * @param managerData the managerData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated managerData,
     * or with status {@code 400 (Bad Request)} if the managerData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the managerData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@permissionService.hasManagerDataRwAccess(#managerData)")
    @PutMapping("/manager-data")
    public ResponseEntity<ManagerData> updateManagerData(@RequestBody ManagerData managerData) throws URISyntaxException {
        log.debug("REST request to update ManagerData : {}", managerData);
        if (managerData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ManagerData result = managerDataService.save(managerData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, managerData.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /manager-data} : get all the managerData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of managerData in body.
     */
    @GetMapping("/manager-data")
    public ResponseEntity<List<ManagerData>> getAllManagerData(ManagerDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ManagerData by criteria: {}", criteria);
        criteria = permissionService.limitAccess(criteria);
        log.debug("criteria after limit access: {}", criteria);
        Page<ManagerData> page = managerDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manager-data/count} : count all the managerData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/manager-data/count")
    public ResponseEntity<Long> countManagerData(ManagerDataCriteria criteria) {
        log.debug("REST request to count ManagerData by criteria: {}", criteria);
        criteria = permissionService.limitAccess(criteria);
        log.debug("criteria after limit access: {}", criteria);
        return ResponseEntity.ok().body(managerDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /manager-data/:id} : get the "id" managerData.
     *
     * @param id the id of the managerData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the managerData, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@permissionService.hasManagerDataRoAccess(#id)")
    @GetMapping("/manager-data/{id}")
    public ResponseEntity<ManagerData> getManagerData(@PathVariable Long id) {
        log.debug("REST request to get ManagerData : {}", id);
        Optional<ManagerData> managerData = managerDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(managerData);
    }

    /**
     * {@code DELETE  /manager-data/:id} : delete the "id" managerData.
     *
     * @param id the id of the managerData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@permissionService.hasManagerDataRwAccess(#id)")
    @DeleteMapping("/manager-data/{id}")
    public ResponseEntity<Void> deleteManagerData(@PathVariable Long id) {
        log.debug("REST request to delete ManagerData : {}", id);
        managerDataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
