package com.idgindigo.pms.logins.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 11:29 AM
 */

@Entity
@DiscriminatorValue(Admin.ADMIN)
public class Admin extends Authentication {
    public static final String ADMIN = "admin";

    @Override
    public String getUserType() {
        return ADMIN;
    }
}
