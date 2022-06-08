package ru.mirea.repository;

import ru.mirea.domain.ManagerData;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ManagerData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManagerDataRepository extends JpaRepository<ManagerData, Long>, JpaSpecificationExecutor<ManagerData> {
}
