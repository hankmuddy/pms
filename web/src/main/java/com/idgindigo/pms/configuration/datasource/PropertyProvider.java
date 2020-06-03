package com.idgindigo.pms.configuration.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 29.10.13 12:55
 */
public interface PropertyProvider {
    Environment getEnvironment();

    @Configuration
    @Profile("dev")
    @PropertySource("classpath:META-INF/jdbc.dev.properties")
    class DevelopmentPropertyProvider implements PropertyProvider {

        @Inject
        private Environment environment;

        @Override
        public Environment getEnvironment() {
            return environment;
        }
    }

    @Configuration
    @Profile("88")
    @PropertySource("classpath:META-INF/jdbc.88.properties")
    class The88PropertyProvider implements PropertyProvider {

        @Inject
        private Environment environment;

        @Override
        public Environment getEnvironment() {
            return environment;
        }
    }

    @Configuration
    @Profile("144")
    @PropertySource("classpath:META-INF/jdbc.144.properties")
    class The144PropertyProvider implements PropertyProvider {

        @Inject
        private Environment environment;

        @Override
        public Environment getEnvironment() {
            return environment;
        }
    }

    @Configuration
    @Profile("amazon")
    @PropertySource("classpath:META-INF/jdbc.amazon.properties")
    class AmazonPropertyProvider implements PropertyProvider {

        @Inject
        private Environment environment;

        @Override
        public Environment getEnvironment() {
            return environment;
        }
    }

}
