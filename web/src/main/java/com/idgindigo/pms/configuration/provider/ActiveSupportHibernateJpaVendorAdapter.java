package com.idgindigo.pms.configuration.provider;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 4:38 PM
 */
public class ActiveSupportHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter {

    @Override
    public HibernatePersistenceProvider getPersistenceProvider() {
        return new ActiveSupportHibernatePersistenceProvider();
    }
}
