package com.idgindigo.pms.configuration.datasource;

import org.hibernate.HibernateException;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 12:02 PM
 */
public class MultiTenantConnectionProviderImpl extends C3P0ConnectionProvider
        implements MultiTenantConnectionProvider {

    @Override
    public Connection getAnyConnection() throws SQLException {
        return super.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        super.closeConnection(connection);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        try {
            connection.createStatement().execute("SET search_path TO  '" + tenantIdentifier + "'");//NOSONAR
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" +
                            tenantIdentifier + "]",
                    e
            );
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        super.closeConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return super.supportsAggressiveRelease();
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return super.isUnwrappableAs(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return super.unwrap(unwrapType);
    }
}
