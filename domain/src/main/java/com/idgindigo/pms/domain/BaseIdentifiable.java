package com.idgindigo.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.audit.EntityLifecycleAudit;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.joda.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author vomel
 * @since 07.11.13 13:40
 */
@Getter
@Setter
@MappedSuperclass
@FilterDef(name = BaseIdentifiable.ACTIVE_FILTER, parameters = @ParamDef(name = "active", type = "boolean"))
@Filter(name = BaseIdentifiable.ACTIVE_FILTER, condition = "active=:active")
@EntityListeners(EntityLifecycleAudit.class)
public abstract class BaseIdentifiable implements Identifiable {
    public static final String ACTIVE_FILTER = "activeFilter";
    public static final String ACTIVE = "active";
    public static final String ID = "id";
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Access(AccessType.PROPERTY)
    private Long id;
    @Column(updatable = false)
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private LocalDateTime createdDate;
    @JsonIgnore
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    @JsonIgnore
    private Boolean active = Boolean.TRUE;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseIdentifiable other = (BaseIdentifiable) obj;
        if (id == null ? other.id != null : !id.equals(other.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @JsonProperty(ACTIVE)
    public Boolean getActive() {
        return active;
    }

    @JsonIgnore
    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}
