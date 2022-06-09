package ru.mirea.service;

import io.github.jhipster.service.filter.LongFilter;
import org.springframework.stereotype.Service;
import ru.mirea.config.ApplicationProperties;
import ru.mirea.domain.ExecutorData;
import ru.mirea.domain.ManagerData;
import ru.mirea.domain.TemporaryAccess;
import ru.mirea.domain.enumeration.EntityClass;
import ru.mirea.domain.enumeration.PermissionType;
import ru.mirea.repository.TemporaryAccessRepository;
import ru.mirea.security.SecurityUtils;
import ru.mirea.service.dto.ExecutorDataCriteria;
import ru.mirea.service.dto.ManagerDataCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PermissionService {

    private final ApplicationProperties applicationProperties;
    private final TemporaryAccessRepository temporaryAccessRepository;

    public PermissionService(ApplicationProperties applicationProperties, TemporaryAccessRepository temporaryAccessRepository) {
        this.applicationProperties = applicationProperties;
        this.temporaryAccessRepository = temporaryAccessRepository;
    }

    public boolean hasExecutorDataRoAccess(Long entityId) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        return isExecutorDataRoleAccessRo() || isTemporaryAccessRo(EntityClass.EXECUTOR_DATA, entityId, login);
    }

    public boolean hasExecutorDataRwAccess(Long entityId) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        return isExecutorDataRoleAccessRw() || isTemporaryAccessRw(EntityClass.EXECUTOR_DATA, entityId, login);
    }

    public boolean hasExecutorDataRwAccess(ExecutorData executorData) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (executorData != null && executorData.getId() != null) {
            return isExecutorDataRoleAccessRw() || isTemporaryAccessRw(EntityClass.EXECUTOR_DATA, executorData.getId(), login);
        } else {
            return isExecutorDataRoleAccessRw();
        }
    }

    public ExecutorDataCriteria limitAccess(ExecutorDataCriteria criteria) {
        if (!isExecutorDataRoleAccessRo()) {
            String login = SecurityUtils.getCurrentUserLogin().orElse(null);
            LocalDate currentDate = LocalDate.now();
            List<TemporaryAccess> temporaryAccesses =
                temporaryAccessRepository.findAllByLoginAndEntityClass(login, EntityClass.EXECUTOR_DATA)
                    .stream()
                    .filter(temporaryAccess -> temporaryAccess.getEndDate() == null || !temporaryAccess.getEndDate().isBefore(currentDate))
                    .filter(temporaryAccess -> temporaryAccess.getPermissionType().equals(PermissionType.RO)
                        || temporaryAccess.getPermissionType().equals(PermissionType.RW))
                    .filter(temporaryAccess -> temporaryAccess.getEntityId() != null)
                    .collect(Collectors.toList());

            LongFilter permissionId = new LongFilter();
            permissionId.setIn(temporaryAccesses.stream().map(TemporaryAccess::getEntityId).collect(Collectors.toList()));
            criteria.setPermissionId(permissionId);
        }
        return criteria;
    }

    public boolean hasManagerDataRoAccess(Long entityId) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        return isManagerDataRoleAccessRo() || isTemporaryAccessRo(EntityClass.MANAGER_DATA, entityId, login);
    }

    public boolean hasManagerDataRwAccess(Long entityId) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        return isManagerDataRoleAccessRw() || isTemporaryAccessRw(EntityClass.MANAGER_DATA, entityId, login);
    }

    public boolean hasManagerDataRwAccess(ManagerData managerData) {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (managerData != null && managerData.getId() != null) {
            return isManagerDataRoleAccessRw() || isTemporaryAccessRw(EntityClass.MANAGER_DATA, managerData.getId(), login);
        } else {
            return isManagerDataRoleAccessRw();
        }
    }

    public ManagerDataCriteria limitAccess(ManagerDataCriteria criteria) {
        if (!isManagerDataRoleAccessRo()) {
            String login = SecurityUtils.getCurrentUserLogin().orElse(null);
            LocalDate currentDate = LocalDate.now();
            List<TemporaryAccess> temporaryAccesses =
                temporaryAccessRepository.findAllByLoginAndEntityClass(login, EntityClass.MANAGER_DATA)
                    .stream()
                    .filter(temporaryAccess -> temporaryAccess.getEndDate() == null || !temporaryAccess.getEndDate().isBefore(currentDate))
                    .filter(temporaryAccess -> temporaryAccess.getPermissionType().equals(PermissionType.RO)
                        || temporaryAccess.getPermissionType().equals(PermissionType.RW))
                    .filter(temporaryAccess -> temporaryAccess.getEntityId() != null)
                    .collect(Collectors.toList());

            LongFilter permissionId = new LongFilter();
            permissionId.setIn(temporaryAccesses.stream().map(TemporaryAccess::getEntityId).collect(Collectors.toList()));
            criteria.setPermissionId(permissionId);
        }
        return criteria;
    }

    private Boolean isManagerDataRoleAccessRo() {
        return getCurrentUserRoles()
            .anyMatch(role -> applicationProperties.getManagerDataAccessRo().contains(role));
    }

    private Boolean isManagerDataRoleAccessRw() {
        return getCurrentUserRoles()
            .anyMatch(role -> applicationProperties.getManagerDataAccessRw().contains(role));
    }

    private Boolean isExecutorDataRoleAccessRo() {
        return getCurrentUserRoles()
            .anyMatch(role -> applicationProperties.getExecutorDataAccessRo().contains(role));
    }

    private Boolean isExecutorDataRoleAccessRw() {
        return getCurrentUserRoles()
            .anyMatch(role -> applicationProperties.getExecutorDataAccessRw().contains(role));
    }

    private Stream<String> getCurrentUserRoles() {
        return SecurityUtils.getCurrentUserRoles();
    }

    private boolean isTemporaryAccessRo(EntityClass entityClass, Long entityId, String login) {
        LocalDate currentDate = LocalDate.now();
        List<TemporaryAccess> temporaryAccesses =
            temporaryAccessRepository.findAllByLoginAndEntityClassAndEntityId(login, entityClass, entityId)
                .stream()
                .filter(temporaryAccess -> temporaryAccess.getEndDate() == null || !temporaryAccess.getEndDate().isBefore(currentDate))
                .filter(temporaryAccess -> temporaryAccess.getPermissionType().equals(PermissionType.RO)
                    || temporaryAccess.getPermissionType().equals(PermissionType.RW))
                .collect(Collectors.toList());
        return !temporaryAccesses.isEmpty();
    }

    private boolean isTemporaryAccessRw(EntityClass entityClass, Long entityId, String login) {
        LocalDate currentDate = LocalDate.now();
        List<TemporaryAccess> temporaryAccesses =
            temporaryAccessRepository.findAllByLoginAndEntityClassAndEntityId(login, entityClass, entityId)
                .stream()
                .filter(temporaryAccess -> temporaryAccess.getEndDate() == null || !temporaryAccess.getEndDate().isBefore(currentDate))
                .filter(temporaryAccess -> temporaryAccess.getPermissionType().equals(PermissionType.RW))
                .collect(Collectors.toList());
        return !temporaryAccesses.isEmpty();
    }
}
