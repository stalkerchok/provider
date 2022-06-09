package ru.mirea.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.mirea.domain.TemporaryAccess;
import ru.mirea.security.AuthoritiesConstants;
import ru.mirea.service.TemporaryAccessService;
import ru.mirea.web.rest.errors.BadRequestAlertException;
import ru.mirea.service.dto.TemporaryAccessCriteria;
import ru.mirea.service.TemporaryAccessQueryService;

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
 * REST controller for managing {@link ru.mirea.domain.TemporaryAccess}.
 */
@RestController
@RequestMapping("/api")
public class TemporaryAccessResource {

    private final Logger log = LoggerFactory.getLogger(TemporaryAccessResource.class);

    private static final String ENTITY_NAME = "temporaryAccess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemporaryAccessService temporaryAccessService;

    private final TemporaryAccessQueryService temporaryAccessQueryService;

    public TemporaryAccessResource(TemporaryAccessService temporaryAccessService, TemporaryAccessQueryService temporaryAccessQueryService) {
        this.temporaryAccessService = temporaryAccessService;
        this.temporaryAccessQueryService = temporaryAccessQueryService;
    }

    /**
     * {@code POST  /temporary-accesses} : Create a new temporaryAccess.
     *
     * @param temporaryAccess the temporaryAccess to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temporaryAccess, or with status {@code 400 (Bad Request)} if the temporaryAccess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping("/temporary-accesses")
    public ResponseEntity<TemporaryAccess> createTemporaryAccess(@RequestBody TemporaryAccess temporaryAccess) throws URISyntaxException {
        log.debug("REST request to save TemporaryAccess : {}", temporaryAccess);
        if (temporaryAccess.getId() != null) {
            throw new BadRequestAlertException("A new temporaryAccess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemporaryAccess result = temporaryAccessService.save(temporaryAccess);
        return ResponseEntity.created(new URI("/api/temporary-accesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temporary-accesses} : Updates an existing temporaryAccess.
     *
     * @param temporaryAccess the temporaryAccess to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temporaryAccess,
     * or with status {@code 400 (Bad Request)} if the temporaryAccess is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temporaryAccess couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PutMapping("/temporary-accesses")
    public ResponseEntity<TemporaryAccess> updateTemporaryAccess(@RequestBody TemporaryAccess temporaryAccess) throws URISyntaxException {
        log.debug("REST request to update TemporaryAccess : {}", temporaryAccess);
        if (temporaryAccess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TemporaryAccess result = temporaryAccessService.save(temporaryAccess);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temporaryAccess.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temporary-accesses} : get all the temporaryAccesses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temporaryAccesses in body.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/temporary-accesses")
    public ResponseEntity<List<TemporaryAccess>> getAllTemporaryAccesses(TemporaryAccessCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TemporaryAccesses by criteria: {}", criteria);
        Page<TemporaryAccess> page = temporaryAccessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /temporary-accesses/count} : count all the temporaryAccesses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/temporary-accesses/count")
    public ResponseEntity<Long> countTemporaryAccesses(TemporaryAccessCriteria criteria) {
        log.debug("REST request to count TemporaryAccesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(temporaryAccessQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /temporary-accesses/:id} : get the "id" temporaryAccess.
     *
     * @param id the id of the temporaryAccess to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temporaryAccess, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/temporary-accesses/{id}")
    public ResponseEntity<TemporaryAccess> getTemporaryAccess(@PathVariable Long id) {
        log.debug("REST request to get TemporaryAccess : {}", id);
        Optional<TemporaryAccess> temporaryAccess = temporaryAccessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temporaryAccess);
    }

    /**
     * {@code DELETE  /temporary-accesses/:id} : delete the "id" temporaryAccess.
     *
     * @param id the id of the temporaryAccess to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @DeleteMapping("/temporary-accesses/{id}")
    public ResponseEntity<Void> deleteTemporaryAccess(@PathVariable Long id) {
        log.debug("REST request to delete TemporaryAccess : {}", id);
        temporaryAccessService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
