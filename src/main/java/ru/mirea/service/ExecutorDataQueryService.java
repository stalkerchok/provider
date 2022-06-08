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

import ru.mirea.domain.ExecutorData;
import ru.mirea.domain.*; // for static metamodels
import ru.mirea.repository.ExecutorDataRepository;
import ru.mirea.service.dto.ExecutorDataCriteria;

/**
 * Service for executing complex queries for {@link ExecutorData} entities in the database.
 * The main input is a {@link ExecutorDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExecutorData} or a {@link Page} of {@link ExecutorData} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExecutorDataQueryService extends QueryService<ExecutorData> {

    private final Logger log = LoggerFactory.getLogger(ExecutorDataQueryService.class);

    private final ExecutorDataRepository executorDataRepository;

    public ExecutorDataQueryService(ExecutorDataRepository executorDataRepository) {
        this.executorDataRepository = executorDataRepository;
    }

    /**
     * Return a {@link List} of {@link ExecutorData} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExecutorData> findByCriteria(ExecutorDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExecutorData> specification = createSpecification(criteria);
        return executorDataRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExecutorData} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExecutorData> findByCriteria(ExecutorDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExecutorData> specification = createSpecification(criteria);
        return executorDataRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExecutorDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExecutorData> specification = createSpecification(criteria);
        return executorDataRepository.count(specification);
    }

    /**
     * Function to convert {@link ExecutorDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExecutorData> createSpecification(ExecutorDataCriteria criteria) {
        Specification<ExecutorData> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExecutorData_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ExecutorData_.name));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), ExecutorData_.data));
            }
        }
        return specification;
    }
}
