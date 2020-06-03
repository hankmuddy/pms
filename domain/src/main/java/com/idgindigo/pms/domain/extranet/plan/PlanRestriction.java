package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 3:28 PM
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class PlanRestriction extends BaseEntity {
    @NotNull
    private String name;
    @Column(updatable = false)
    private Long pid;
}
