package com.idgindigo.pms.logins.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 11:25 AM
 */
@Entity
@DiscriminatorValue(Manager.MANAGER)
@Getter
@Setter
public class Manager extends Authentication {
    public static final String MANAGER = "manager";
    @ManyToOne
    @NotNull
    private ManagerSupervisor supervisor;

    @Override
    public String getUserType() {
        return MANAGER;
    }
}
