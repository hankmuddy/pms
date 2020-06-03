package com.idgindigo.pms.configuration;

import com.idgindigo.pms.configuration.datasource.PropertyProvider;
import com.idgindigo.pms.configuration.dialect.TestExtendedHibernateJpaDialect;
import com.idgindigo.pms.configuration.provider.ActiveSupportHibernateJpaVendorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author vomel
 * @since 29.10.13 14:59
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.idgindigo.pms.repository", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.idgindigo.pms.configuration.datasource")
@EnableTransactionManagement
@ActiveProfiles("dev")
public class TestJpaConfig extends JpaConfig {

    @Inject
    private PropertyProvider propertyProvider;

    @Override
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        return getFactory(dataSource(), propertyProvider.getEnvironment(), "com.idgindigo.pms.domain");
    }

    static LocalContainerEntityManagerFactoryBean getFactory(DataSource dataSource, PropertyResolver environment, String packagesToScan) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaDialect(new TestExtendedHibernateJpaDialect());
        entityManagerFactory.setPackagesToScan(packagesToScan);

        HibernateJpaVendorAdapter adapter = new ActiveSupportHibernateJpaVendorAdapter();
        adapter.setDatabase(Database.HSQL);
        adapter.setGenerateDdl(Boolean.TRUE);
        adapter.setShowSql(Boolean.FALSE);

        entityManagerFactory.setJpaVendorAdapter(adapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
        jpaProperties.setProperty("javax.persistence.jdbc.url", "jdbc:hsqldb:mem:testdb");
        jpaProperties.setProperty("javax.persistence.jdbc.user", "sa");
        jpaProperties.setProperty("javax.persistence.jdbc.password", "");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.validator.apply_to_ddl", environment.getProperty("datasource.applyValidationConstraintsToDdl"));
        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return getDataSource();
    }

    static DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

}
