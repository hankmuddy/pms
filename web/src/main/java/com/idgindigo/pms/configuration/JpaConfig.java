package com.idgindigo.pms.configuration;

import com.idgindigo.pms.audit.EventListenerImpl;
import com.idgindigo.pms.configuration.datasource.PropertyProvider;
import com.idgindigo.pms.configuration.dialect.ExtendedHibernateJpaDialect;
import com.idgindigo.pms.configuration.provider.ActiveSupportHibernateJpaVendorAdapter;
import com.idgindigo.pms.domain.audit.BaseEntityAudit;
import com.idgindigo.pms.domain.audit.EventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import java.util.Properties;

/**
 * @author vomel
 * @since 29.10.13 12:57
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.idgindigo.pms.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@ComponentScan(basePackages = {"com.idgindigo.pms.configuration.datasource"})
public class JpaConfig {
    public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";

    @Inject
    private PropertyProvider propertyProvider;

    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManager() {
        Environment environment = propertyProvider.getEnvironment();
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("com.idgindigo.pms.domain");

        HibernateJpaVendorAdapter adapter = new ActiveSupportHibernateJpaVendorAdapter();
        entityManagerFactory.setJpaDialect(new ExtendedHibernateJpaDialect());
        adapter.setShowSql(Boolean.TRUE);

        entityManagerFactory.setJpaVendorAdapter(adapter);

        entityManagerFactory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", environment.getProperty("datasource.dialect"));
        jpaProperties.setProperty("hibernate.ejb.entitymanager_factory_name", ENTITY_MANAGER_FACTORY);
        jpaProperties.setProperty("hibernate.multiTenancy", environment.getProperty("datasource.multitenancy.strategy"));
        jpaProperties.setProperty("hibernate.tenant_identifier_resolver", environment.getProperty("datasource.tenant_resolver"));
        jpaProperties.setProperty("hibernate.multi_tenant_connection_provider", environment.getProperty("datasource.master.provider"));
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
        jpaProperties.setProperty("hibernate.show_sql", environment.getProperty("datasource.show_sql"));
        jpaProperties.setProperty("hibernate.format_sql", environment.getProperty("datasource.format_sql"));
//        jpaProperties.setProperty("hibernate.show_sql", "true");
//        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.validator.apply_to_ddl", environment.getProperty("datasource.applyValidationConstraintsToDdl"));
        jpaProperties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        jpaProperties.setProperty("jadira.usertype.databaseZone", "jvm");
        jpaProperties.setProperty("jadira.usertype.javaZone", "jvm");
        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }

    @Bean
    public EventListener injector() {
        return new EventListenerImpl();
    }

    @Bean
    public BaseEntityAudit listener() {
        return new BaseEntityAudit();
    }

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManager().getObject());
    }

}