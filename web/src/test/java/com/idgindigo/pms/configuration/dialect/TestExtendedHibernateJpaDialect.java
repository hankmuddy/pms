package com.idgindigo.pms.configuration.dialect;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 5:45 PM
 */
public class TestExtendedHibernateJpaDialect extends ExtendedHibernateJpaDialect {
    /*
    Required for hsql as it doesnt support read-only transactions
     */
    @Override
    protected void setAdditionalProperties(Connection connection) {
        try {
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn`t alter provided connection " + connection);
        }
    }
}
