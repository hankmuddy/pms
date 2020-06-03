package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.ApprovableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 1:46 PM
 */
@Entity
@Table(name = "plan")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public class BasePlan extends ApprovableEntity {

    @NotNull
    @Column(unique = true)
    private String name;

    private Long pid;//wubook plan id
}