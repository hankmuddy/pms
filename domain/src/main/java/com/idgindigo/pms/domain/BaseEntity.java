package com.idgindigo.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.idgindigo.pms.domain.audit.BaseEntityAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author vomel
 * @since 29.10.13 13:23
 */
@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(BaseEntityAudit.class)
public class BaseEntity extends BaseIdentifiable {
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_BY = "updatedBy";
    public static final String CONSTRAINT_VALUE_IS_LESS_THAN_MIN = "constraint.valueIsLessThanMin";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CREATED_BY, updatable = false)
    @JsonIgnore
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UPDATED_BY)
    @JsonIgnore
    private User updatedBy;

    public interface BaseView {
    }

    public interface ListView extends BaseView {
    }

    public interface SoloView extends BaseView {
    }

    public interface NoView extends BaseView {
    }

}
