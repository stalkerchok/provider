package ru.mirea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.mirea.domain.TemporaryAccess;
import ru.mirea.domain.enumeration.EntityClass;

import java.util.List;

/**
 * Spring Data  repository for the TemporaryAccess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemporaryAccessRepository extends JpaRepository<TemporaryAccess, Long>, JpaSpecificationExecutor<TemporaryAccess> {
    List<TemporaryAccess> findAllByLoginAndEntityClass(String login, EntityClass entityClass);
    List<TemporaryAccess> findAllByLoginAndEntityClassAndEntityId(String login, EntityClass entityClass, Long entityId);
}
