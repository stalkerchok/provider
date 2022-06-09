package ru.mirea.service;

import ru.mirea.domain.TemporaryAccess;
import ru.mirea.repository.TemporaryAccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TemporaryAccess}.
 */
@Service
@Transactional
public class TemporaryAccessService {

    private final Logger log = LoggerFactory.getLogger(TemporaryAccessService.class);

    private final TemporaryAccessRepository temporaryAccessRepository;

    public TemporaryAccessService(TemporaryAccessRepository temporaryAccessRepository) {
        this.temporaryAccessRepository = temporaryAccessRepository;
    }

    /**
     * Save a temporaryAccess.
     *
     * @param temporaryAccess the entity to save.
     * @return the persisted entity.
     */
    public TemporaryAccess save(TemporaryAccess temporaryAccess) {
        log.debug("Request to save TemporaryAccess : {}", temporaryAccess);
        return temporaryAccessRepository.save(temporaryAccess);
    }

    /**
     * Get all the temporaryAccesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemporaryAccess> findAll(Pageable pageable) {
        log.debug("Request to get all TemporaryAccesses");
        return temporaryAccessRepository.findAll(pageable);
    }


    /**
     * Get one temporaryAccess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemporaryAccess> findOne(Long id) {
        log.debug("Request to get TemporaryAccess : {}", id);
        return temporaryAccessRepository.findById(id);
    }

    /**
     * Delete the temporaryAccess by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TemporaryAccess : {}", id);
        temporaryAccessRepository.deleteById(id);
    }
}
