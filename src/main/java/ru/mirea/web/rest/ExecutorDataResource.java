package ru.mirea.web.rest;

import ru.mirea.domain.ExecutorData;
import ru.mirea.service.ExecutorDataService;
import ru.mirea.web.rest.errors.BadRequestAlertException;
import ru.mirea.service.dto.ExecutorDataCriteria;
import ru.mirea.service.ExecutorDataQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ru.mirea.domain.ExecutorData}.
 */
@RestController
@RequestMapping("/api")
public class ExecutorDataResource {

    private final Logger log = LoggerFactory.getLogger(ExecutorDataResource.class);

    private static final String ENTITY_NAME = "executorData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutorDataService executorDataService;

    private final ExecutorDataQueryService executorDataQueryService;

    public ExecutorDataResource(ExecutorDataService executorDataService, ExecutorDataQueryService executorDataQueryService) {
        this.executorDataService = executorDataService;
        this.executorDataQueryService = executorDataQueryService;
    }

    /**
     * {@code POST  /executor-data} : Create a new executorData.
     *
     * @param executorData the executorData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new executorData, or with status {@code 400 (Bad Request)} if the executorData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/executor-data")
    public ResponseEntity<ExecutorData> createExecutorData(@RequestBody ExecutorData executorData) throws URISyntaxException {
        log.debug("REST request to save ExecutorData : {}", executorData);
        if (executorData.getId() != null) {
            throw new BadRequestAlertException("A new executorData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExecutorData result = executorDataService.save(executorData);
        return ResponseEntity.created(new URI("/api/executor-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /executor-data} : Updates an existing executorData.
     *
     * @param executorData the executorData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executorData,
     * or with status {@code 400 (Bad Request)} if the executorData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the executorData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/executor-data")
    public ResponseEntity<ExecutorData> updateExecutorData(@RequestBody ExecutorData executorData) throws URISyntaxException {
        log.debug("REST request to update ExecutorData : {}", executorData);
        if (executorData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExecutorData result = executorDataService.save(executorData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executorData.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /executor-data} : get all the executorData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executorData in body.
     */
    @GetMapping("/executor-data")
    public ResponseEntity<List<ExecutorData>> getAllExecutorData(ExecutorDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExecutorData by criteria: {}", criteria);
        Page<ExecutorData> page = executorDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /executor-data/count} : count all the executorData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/executor-data/count")
    public ResponseEntity<Long> countExecutorData(ExecutorDataCriteria criteria) {
        log.debug("REST request to count ExecutorData by criteria: {}", criteria);
        return ResponseEntity.ok().body(executorDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /executor-data/:id} : get the "id" executorData.
     *
     * @param id the id of the executorData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the executorData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/executor-data/{id}")
    public ResponseEntity<ExecutorData> getExecutorData(@PathVariable Long id) {
        log.debug("REST request to get ExecutorData : {}", id);
        Optional<ExecutorData> executorData = executorDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executorData);
    }

    /**
     * {@code DELETE  /executor-data/:id} : delete the "id" executorData.
     *
     * @param id the id of the executorData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/executor-data/{id}")
    public ResponseEntity<Void> deleteExecutorData(@PathVariable Long id) {
        log.debug("REST request to delete ExecutorData : {}", id);
        executorDataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
