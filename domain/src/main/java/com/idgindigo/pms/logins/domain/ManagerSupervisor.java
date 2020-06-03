package com.idgindigo.pms.logins.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 11:28 AM
 */
@Entity
@DiscriminatorValue("MANAGER_SUPERVISOR")//TODO fixme
public class ManagerSupervisor extends Authentication {
    public static final String MANAGER_SUPERVISOR = "managerSupervisor";

    @Override
    public String getUserType() {
        return MANAGER_SUPERVISOR;
    }
}
