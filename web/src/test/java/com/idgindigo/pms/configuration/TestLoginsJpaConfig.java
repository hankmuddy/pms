package com.idgindigo.pms.configuration;

import com.idgindigo.pms.configuration.datasource.PropertyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;

import static com.idgindigo.pms.configuration.TestJpaConfig.getDataSource;
import static com.idgindigo.pms.configuration.TestJpaConfig.getFactory;

/**
 * @author valentyn_vakatsiienko
 * @since 11/7/13 11:04 AM
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.idgindigo.pms.logins.repository", entityManagerFactoryRef = "loginsEntityManagerFactory", transactionManagerRef = LoginsJpaConfig.TRANSACTION_MANAGER)
@EnableTransactionManagement
@ActiveProfiles("dev")
public class TestLoginsJpaConfig extends LoginsJpaConfig {

    @Inject
    private PropertyProvider propertyProvider;

    @Override
    @Bean(name = "loginsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        return getFactory(dataSource(), propertyProvider.getEnvironment(), "com.idgindigo.pms.logins.domain");
    }

    @Bean
    public DataSource dataSource() {
        return getDataSource();
    }
}
