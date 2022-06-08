package ru.mirea.service;

import ru.mirea.domain.ManagerData;
import ru.mirea.repository.ManagerDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ManagerData}.
 */
@Service
@Transactional
public class ManagerDataService {

    private final Logger log = LoggerFactory.getLogger(ManagerDataService.class);

    private final ManagerDataRepository managerDataRepository;

    public ManagerDataService(ManagerDataRepository managerDataRepository) {
        this.managerDataRepository = managerDataRepository;
    }

    /**
     * Save a managerData.
     *
     * @param managerData the entity to save.
     * @return the persisted entity.
     */
    public ManagerData save(ManagerData managerData) {
        log.debug("Request to save ManagerData : {}", managerData);
        return managerDataRepository.save(managerData);
    }

    /**
     * Get all the managerData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ManagerData> findAll(Pageable pageable) {
        log.debug("Request to get all ManagerData");
        return managerDataRepository.findAll(pageable);
    }


    /**
     * Get one managerData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ManagerData> findOne(Long id) {
        log.debug("Request to get ManagerData : {}", id);
        return managerDataRepository.findById(id);
    }

    /**
     * Delete the managerData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ManagerData : {}", id);
        managerDataRepository.deleteById(id);
    }
}
