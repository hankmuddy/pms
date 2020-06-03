package com.idgindigo.pms.service.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author valentyn_vakatsiienko
 * @since 11/14/13 1:02 PM
 */
@Service
public class TenantService {
    private static final Logger logger = LoggerFactory.getLogger(TenantService.class);

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initTenant(String tenantId) {
        logger.info("Instantiating tenant with id: {}", tenantId);
        entityManager.createNativeQuery("select public.init_tenant('" + tenantId + "')").getSingleResult();
        logger.info("Successfully instantiated tenant with id: {}", tenantId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dropTenant(String tenantId) {
        logger.info("Dropping tenant with id: {}", tenantId);
        entityManager.createNativeQuery("select public.drop_tenant('" + tenantId + "')").getSingleResult();
        logger.info("Successfully dropped tenant with id: {}", tenantId);
    }

}
