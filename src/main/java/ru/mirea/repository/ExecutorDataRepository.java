package ru.mirea.repository;

import ru.mirea.domain.ExecutorData;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExecutorData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutorDataRepository extends JpaRepository<ExecutorData, Long>, JpaSpecificationExecutor<ExecutorData> {
}
