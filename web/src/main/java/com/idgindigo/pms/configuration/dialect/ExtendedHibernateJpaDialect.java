package com.idgindigo.pms.configuration.dialect;

import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 4:58 PM
 */
public class ExtendedHibernateJpaDialect extends HibernateJpaDialect {

    @Override
    public Object beginTransaction(final EntityManager entityManager, final TransactionDefinition definition) throws PersistenceException, SQLException, TransactionException {
        final Connection[] conn = new Connection[1];
        final Integer[] previousIsolationLevel = new Integer[1];
        Session session = (Session) entityManager.getDelegate();
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            getSession(entityManager).getTransaction().setTimeout(definition.getTimeout());
        }

        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                previousIsolationLevel[0] = DataSourceUtils.prepareConnectionForTransaction(connection, definition);
                setAdditionalProperties(connection);
                conn[0] = connection;
            }
        });

        entityManager.getTransaction().begin();

        Object transactionDataFromHibernateJpaTemplate = prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());

        return new IsolationSupportSessionTransactionData(transactionDataFromHibernateJpaTemplate, previousIsolationLevel[0], conn[0]);
    }

    protected void setAdditionalProperties(Connection connection) {
    }


    @Override
    public void cleanupTransaction(Object transactionData) {
        IsolationSupportSessionTransactionData isolationSupportSessionTransactionData = (IsolationSupportSessionTransactionData) transactionData;

        super.cleanupTransaction(isolationSupportSessionTransactionData.getSessionTransactionDataFromHibernateTemplate());
        isolationSupportSessionTransactionData.resetIsolationLevel();
        try {
            isolationSupportSessionTransactionData.connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new IllegalStateException("couldn`t reset readonly transaction property: " + e);
        }
    }

    private static class IsolationSupportSessionTransactionData {

        private final Object sessionTransactionDataFromHibernateJpaTemplate;
        private final Integer previousIsolationLevel;
        private final Connection connection;

        public IsolationSupportSessionTransactionData(Object sessionTransactionDataFromHibernateJpaTemplate, Integer previousIsolationLevel, Connection connection) {
            this.sessionTransactionDataFromHibernateJpaTemplate = sessionTransactionDataFromHibernateJpaTemplate;
            this.previousIsolationLevel = previousIsolationLevel;
            this.connection = connection;
        }

        public void resetIsolationLevel() {
            if (this.previousIsolationLevel != null) {
                DataSourceUtils.resetConnectionAfterTransaction(connection, previousIsolationLevel);
            }
        }

        public Object getSessionTransactionDataFromHibernateTemplate() {
            return this.sessionTransactionDataFromHibernateJpaTemplate;
        }

    }

}
