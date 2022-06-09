package ru.mirea.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import ru.mirea.domain.enumeration.PermissionType;
import ru.mirea.domain.enumeration.Role;
import ru.mirea.domain.enumeration.EntityClass;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link ru.mirea.domain.TemporaryAccess} entity. This class is used
 * in {@link ru.mirea.web.rest.TemporaryAccessResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /temporary-accesses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TemporaryAccessCriteria implements Serializable, Criteria {
    /**
     * Class for filtering PermissionType
     */
    public static class PermissionTypeFilter extends Filter<PermissionType> {

        public PermissionTypeFilter() {
        }

        public PermissionTypeFilter(PermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public PermissionTypeFilter copy() {
            return new PermissionTypeFilter(this);
        }

    }
    /**
     * Class for filtering Role
     */
    public static class RoleFilter extends Filter<Role> {

        public RoleFilter() {
        }

        public RoleFilter(RoleFilter filter) {
            super(filter);
        }

        @Override
        public RoleFilter copy() {
            return new RoleFilter(this);
        }

    }
    /**
     * Class for filtering EntityClass
     */
    public static class EntityClassFilter extends Filter<EntityClass> {

        public EntityClassFilter() {
        }

        public EntityClassFilter(EntityClassFilter filter) {
            super(filter);
        }

        @Override
        public EntityClassFilter copy() {
            return new EntityClassFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    private LocalDateFilter endDate;

    private PermissionTypeFilter permissionType;

    private RoleFilter role;

    private EntityClassFilter entityClass;

    private LongFilter entityId;

    public TemporaryAccessCriteria() {
    }

    public TemporaryAccessCriteria(TemporaryAccessCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.permissionType = other.permissionType == null ? null : other.permissionType.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.entityClass = other.entityClass == null ? null : other.entityClass.copy();
        this.entityId = other.entityId == null ? null : other.entityId.copy();
    }

    @Override
    public TemporaryAccessCriteria copy() {
        return new TemporaryAccessCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public PermissionTypeFilter getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionTypeFilter permissionType) {
        this.permissionType = permissionType;
    }

    public RoleFilter getRole() {
        return role;
    }

    public void setRole(RoleFilter role) {
        this.role = role;
    }

    public EntityClassFilter getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(EntityClassFilter entityClass) {
        this.entityClass = entityClass;
    }

    public LongFilter getEntityId() {
        return entityId;
    }

    public void setEntityId(LongFilter entityId) {
        this.entityId = entityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TemporaryAccessCriteria that = (TemporaryAccessCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(permissionType, that.permissionType) &&
            Objects.equals(role, that.role) &&
            Objects.equals(entityClass, that.entityClass) &&
            Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        login,
        endDate,
        permissionType,
        role,
        entityClass,
        entityId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemporaryAccessCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (permissionType != null ? "permissionType=" + permissionType + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (entityClass != null ? "entityClass=" + entityClass + ", " : "") +
                (entityId != null ? "entityId=" + entityId + ", " : "") +
            "}";
    }

}
