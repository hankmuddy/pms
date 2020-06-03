package com.idgindigo.pms.configuration;

import com.idgindigo.pms.configuration.datasource.PropertyProvider;
import com.idgindigo.pms.configuration.dialect.ExtendedHibernateJpaDialect;
import com.idgindigo.pms.configuration.provider.ActiveSupportHibernateJpaVendorAdapter;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author valentyn_vakatsiienko
 * @since 11/13/13 5:02 PM
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.idgindigo.pms.repository", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = {"com.idgindigo.pms.configuration.datasource"})
@EnableTransactionManagement
@ActiveProfiles("dev")
public class PostgresDbTestJpaConfig extends JpaConfig {

    @Inject
    private PropertyProvider propertyProvider;
    private AbstractEntityManagerFactoryBean entityManagerFactory;

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        Environment environment = propertyProvider.getEnvironment();
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("com.idgindigo.pms.domain");

        HibernateJpaVendorAdapter adapter = new ActiveSupportHibernateJpaVendorAdapter();
        entityManagerFactory.setJpaDialect(new ExtendedHibernateJpaDialect());
        adapter.setShowSql(Boolean.TRUE);
        adapter.setGenerateDdl(Boolean.TRUE);

        entityManagerFactory.setJpaVendorAdapter(adapter);

        entityManagerFactory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.default_schema", "test");
        jpaProperties.setProperty("hibernate.dialect", environment.getProperty("datasource.dialect"));
        jpaProperties.setProperty("hibernate.connection.driver_class", environment.getProperty("datasource.driver"));
        jpaProperties.setProperty("hibernate.connection.url", environment.getProperty("datasource.url"));
        jpaProperties.setProperty("hibernate.connection.username", environment.getProperty("datasource.username"));
        jpaProperties.setProperty("hibernate.connection.password", environment.getProperty("datasource.password"));
        jpaProperties.setProperty("hibernate.c3p0.min_size", environment.getProperty("datasource.MinPoolSize"));
        jpaProperties.setProperty("hibernate.c3p0.max_size", environment.getProperty("datasource.MaxPoolSize"));
        jpaProperties.setProperty("hibernate.c3p0.acquire_increment", environment.getProperty("datasource.AcquireIncrement"));
        jpaProperties.setProperty("hibernate.max_fetch_depth", environment.getProperty("datasource.fetch_depth"));
        jpaProperties.setProperty("hibernate.jdbc.fetch_size", environment.getProperty("datasource.fetch_size"));
        jpaProperties.setProperty("hibernate.jdbc.batch_size", environment.getProperty("datasource.batch_size"));
        jpaProperties.setProperty("hibernate.dialect", environment.getProperty("datasource.dialect"));
        jpaProperties.setProperty("hibernate.show_sql", environment.getProperty("datasource.show_sql"));
        jpaProperties.setProperty("hibernate.format_sql", environment.getProperty("datasource.format_sql"));
//        jpaProperties.setProperty("hibernate.show_sql", "true");
//        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.validator.apply_to_ddl", environment.getProperty("datasource.applyValidationConstraintsToDdl"));
        jpaProperties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        jpaProperties.setProperty("jadira.usertype.databaseZone", "jvm");
        jpaProperties.setProperty("jadira.usertype.javaZone", "jvm");

        entityManagerFactory.setJpaProperties(jpaProperties);
        this.entityManagerFactory = entityManagerFactory;
        return entityManagerFactory;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
        DataSource dataSource = SessionFactoryUtils.getDataSource(entityManager.unwrap(Session.class).getSessionFactory());
        return dataSource;
    }

}
