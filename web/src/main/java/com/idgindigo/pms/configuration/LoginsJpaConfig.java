package com.idgindigo.pms.configuration;

import com.idgindigo.pms.configuration.datasource.PropertyProvider;
import com.idgindigo.pms.configuration.provider.ActiveSupportHibernateJpaVendorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import java.util.Properties;

/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 4:12 PM
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.idgindigo.pms.logins.repository", entityManagerFactoryRef = "loginsEntityManagerFactory", transactionManagerRef = LoginsJpaConfig.TRANSACTION_MANAGER)
@EnableTransactionManagement
public class LoginsJpaConfig {
    public static final String LOGINS_ENTITY_MANAGER_FACTORY = "loginsEntityManagerFactory";
    public static final String TRANSACTION_MANAGER = "loginsTransactionManager";

    @Inject
    private PropertyProvider propertyProvider;

    @Bean(name = LOGINS_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManager() {
        Environment environment = propertyProvider.getEnvironment();
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("com.idgindigo.pms.logins.domain");

        HibernateJpaVendorAdapter adapter = new ActiveSupportHibernateJpaVendorAdapter();
        adapter.setShowSql(Boolean.TRUE);
        adapter.setGenerateDdl(Boolean.FALSE);

        entityManagerFactory.setJpaVendorAdapter(adapter);

        entityManagerFactory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.default_schema", "logins");
        jpaProperties.setProperty("hibernate.dialect", environment.getProperty("datasource.dialect"));
        jpaProperties.setProperty("hibernate.ejb.entitymanager_factory_name", LOGINS_ENTITY_MANAGER_FACTORY);
        jpaProperties.setProperty("hibernate.connection.provider_class", environment.getProperty("datasource.logins.provider"));
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
        jpaProperties.setProperty("hibernate.validator.apply_to_ddl", environment.getProperty("datasource.applyValidationConstraintsToDdl"));
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("datasource.generateDdl"));
        jpaProperties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        jpaProperties.setProperty("jadira.usertype.databaseZone", "jvm");
        jpaProperties.setProperty("jadira.usertype.javaZone", "jvm");

        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }

    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager() {

        return new JpaTransactionManager(entityManager().getObject());

    }
}
