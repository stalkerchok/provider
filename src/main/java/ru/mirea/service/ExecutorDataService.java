package ru.mirea.service;

import ru.mirea.domain.ExecutorData;
import ru.mirea.repository.ExecutorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExecutorData}.
 */
@Service
@Transactional
public class ExecutorDataService {

    private final Logger log = LoggerFactory.getLogger(ExecutorDataService.class);

    private final ExecutorDataRepository executorDataRepository;

    public ExecutorDataService(ExecutorDataRepository executorDataRepository) {
        this.executorDataRepository = executorDataRepository;
    }

    /**
     * Save a executorData.
     *
     * @param executorData the entity to save.
     * @return the persisted entity.
     */
    public ExecutorData save(ExecutorData executorData) {
        log.debug("Request to save ExecutorData : {}", executorData);
        return executorDataRepository.save(executorData);
    }

    /**
     * Get all the executorData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExecutorData> findAll(Pageable pageable) {
        log.debug("Request to get all ExecutorData");
        return executorDataRepository.findAll(pageable);
    }


    /**
     * Get one executorData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExecutorData> findOne(Long id) {
        log.debug("Request to get ExecutorData : {}", id);
        return executorDataRepository.findById(id);
    }

    /**
     * Delete the executorData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExecutorData : {}", id);
        executorDataRepository.deleteById(id);
    }
}
