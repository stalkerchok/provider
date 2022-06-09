package ru.mirea.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import ru.mirea.domain.enumeration.PermissionType;

import ru.mirea.domain.enumeration.Role;

import ru.mirea.domain.enumeration.EntityClass;

/**
 * A TemporaryAccess.
 */
@Entity
@Table(name = "temporary_access")
public class TemporaryAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type")
    private PermissionType permissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_class")
    private EntityClass entityClass;

    @Column(name = "entity_id")
    private Long entityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public TemporaryAccess login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public TemporaryAccess endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public TemporaryAccess permissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
        return this;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public Role getRole() {
        return role;
    }

    public TemporaryAccess role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public EntityClass getEntityClass() {
        return entityClass;
    }

    public TemporaryAccess entityClass(EntityClass entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public void setEntityClass(EntityClass entityClass) {
        this.entityClass = entityClass;
    }

    public Long getEntityId() {
        return entityId;
    }

    public TemporaryAccess entityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemporaryAccess)) {
            return false;
        }
        return id != null && id.equals(((TemporaryAccess) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemporaryAccess{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", permissionType='" + getPermissionType() + "'" +
            ", role='" + getRole() + "'" +
            ", entityClass='" + getEntityClass() + "'" +
            ", entityId=" + getEntityId() +
            "}";
    }
}
