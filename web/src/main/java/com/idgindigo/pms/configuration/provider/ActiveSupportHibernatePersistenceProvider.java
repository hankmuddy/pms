package com.idgindigo.pms.configuration.provider;

import com.idgindigo.pms.domain.BaseEntity;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.internal.metamodel.EntityTypeImpl;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 4:39 PM
 */
public class ActiveSupportHibernatePersistenceProvider extends HibernatePersistenceProvider {

    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        return wrapEntityManagerFactory(super.createEntityManagerFactory(persistenceUnitName, properties));
    }

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
        return wrapEntityManagerFactory(super.createContainerEntityManagerFactory(info, properties));
    }

    private EntityManagerFactoryWrapper wrapEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory == null
                ? null
                : new EntityManagerFactoryWrapper((HibernateEntityManagerFactory) entityManagerFactory);
    }

    //Wrapper class for EntityManagerFactory which enables tenantFilter for every user except anonymous and unauthenticated
    public static class EntityManagerFactoryWrapper implements HibernateEntityManagerFactory {
        private HibernateEntityManagerFactory wrappedEntityManagerFactory;

        public EntityManagerFactoryWrapper(HibernateEntityManagerFactory entityManagerFactory) {
            wrappedEntityManagerFactory = entityManagerFactory;
        }

        @Override
        public EntityManager createEntityManager() {
            return initSession(wrappedEntityManagerFactory.createEntityManager());
        }

        @Override
        public EntityManager createEntityManager(Map properties) {
            return initSession(wrappedEntityManagerFactory.createEntityManager(properties));
        }

        @Override
        public EntityManager createEntityManager(SynchronizationType synchronizationType) {
            return wrappedEntityManagerFactory.createEntityManager(synchronizationType);
        }

        @Override
        public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
            return wrappedEntityManagerFactory.createEntityManager(synchronizationType, map);
        }

        //This is where the activeFilter is being activated
        private static EntityManager initSession(EntityManager entityManager) {

            Session session = ((HibernateEntityManager) entityManager).getSession();
            if (session.getEnabledFilter(BaseEntity.ACTIVE_FILTER) == null) {
                Filter activeFilter = session.enableFilter(BaseEntity.ACTIVE_FILTER);
                activeFilter.setParameter("active", Boolean.TRUE);
            }

            return entityManager;
        }

        @Override
        public SessionFactory getSessionFactory() {
            return wrappedEntityManagerFactory.getSessionFactory();
        }

        @Override
        public EntityTypeImpl getEntityTypeByName(String s) {
            return wrappedEntityManagerFactory.getEntityTypeByName(s);
        }


        @Override
        public CriteriaBuilder getCriteriaBuilder() {
            return wrappedEntityManagerFactory.getCriteriaBuilder();
        }

        @Override
        public Metamodel getMetamodel() {
            return wrappedEntityManagerFactory.getMetamodel();
        }

        @Override
        public boolean isOpen() {
            return wrappedEntityManagerFactory.isOpen();
        }

        @Override
        public void close() {
            wrappedEntityManagerFactory.close();
        }

        @Override
        public Map<String, Object> getProperties() {
            return wrappedEntityManagerFactory.getProperties();
        }

        @Override
        public Cache getCache() {
            return wrappedEntityManagerFactory.getCache();
        }

        @Override
        public PersistenceUnitUtil getPersistenceUnitUtil() {
            return wrappedEntityManagerFactory.getPersistenceUnitUtil();
        }

        @Override
        public void addNamedQuery(String s, Query query) {
            wrappedEntityManagerFactory.addNamedQuery(s, query);
        }

        @Override
        public <T> T unwrap(Class<T> tClass) {
            return wrappedEntityManagerFactory.unwrap(tClass);
        }

        @Override
        public <T> void addNamedEntityGraph(String s, EntityGraph<T> tEntityGraph) {
            wrappedEntityManagerFactory.addNamedEntityGraph(s, tEntityGraph);
        }
    }

}
