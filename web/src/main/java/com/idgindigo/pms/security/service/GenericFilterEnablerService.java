package com.idgindigo.pms.security.service;

import com.idgindigo.pms.configuration.LoginsJpaConfig;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.security.SecurityUtils;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 6:03 PM
 */
public abstract class GenericFilterEnablerService {
    private final Map<String, String> userTypeToFilter = new HashMap<>();

    @PersistenceContext(unitName = LoginsJpaConfig.LOGINS_ENTITY_MANAGER_FACTORY)
    private EntityManager entityManager;

    public void put(String userType, String filter) {
        userTypeToFilter.put(userType, filter);
    }

    public void enableFilter(String userType) {
        if (userTypeToFilter.containsKey(userType)) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter(userTypeToFilter.get(userType)).setParameter(BaseIdentifiable.ID, SecurityUtils.getUserDetails().getAuthentication().getId());
        }
    }
}
