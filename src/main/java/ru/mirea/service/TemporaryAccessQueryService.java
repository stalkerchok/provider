package ru.mirea.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import ru.mirea.domain.TemporaryAccess;
import ru.mirea.domain.*; // for static metamodels
import ru.mirea.repository.TemporaryAccessRepository;
import ru.mirea.service.dto.TemporaryAccessCriteria;

/**
 * Service for executing complex queries for {@link TemporaryAccess} entities in the database.
 * The main input is a {@link TemporaryAccessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemporaryAccess} or a {@link Page} of {@link TemporaryAccess} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemporaryAccessQueryService extends QueryService<TemporaryAccess> {

    private final Logger log = LoggerFactory.getLogger(TemporaryAccessQueryService.class);

    private final TemporaryAccessRepository temporaryAccessRepository;

    public TemporaryAccessQueryService(TemporaryAccessRepository temporaryAccessRepository) {
        this.temporaryAccessRepository = temporaryAccessRepository;
    }

    /**
     * Return a {@link List} of {@link TemporaryAccess} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemporaryAccess> findByCriteria(TemporaryAccessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TemporaryAccess> specification = createSpecification(criteria);
        return temporaryAccessRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TemporaryAccess} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemporaryAccess> findByCriteria(TemporaryAccessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TemporaryAccess> specification = createSpecification(criteria);
        return temporaryAccessRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemporaryAccessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TemporaryAccess> specification = createSpecification(criteria);
        return temporaryAccessRepository.count(specification);
    }

    /**
     * Function to convert {@link TemporaryAccessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TemporaryAccess> createSpecification(TemporaryAccessCriteria criteria) {
        Specification<TemporaryAccess> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TemporaryAccess_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), TemporaryAccess_.login));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), TemporaryAccess_.endDate));
            }
            if (criteria.getPermissionType() != null) {
                specification = specification.and(buildSpecification(criteria.getPermissionType(), TemporaryAccess_.permissionType));
            }
            if (criteria.getEntityClass() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityClass(), TemporaryAccess_.entityClass));
            }
            if (criteria.getEntityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntityId(), TemporaryAccess_.entityId));
            }
        }
        return specification;
    }
}
