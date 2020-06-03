package com.idgindigo.pms.configuration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.inject.Inject;
import java.util.Properties;

/**
 * @author valentyn_vakatsiienko
 * @since 3/20/14 11:58 AM
 */
public interface MailerProvider {

    JavaMailSender mailSender();

    Environment getEnvironment();

    abstract class GenericMailerProvider implements MailerProvider {
        @Inject
        private Environment environment;

        @Override
        @Bean
        public JavaMailSender mailSender() {

            JavaMailSenderImpl sender = new JavaMailSenderImpl();

            sender.setHost(environment.getProperty("mail.host"));
            sender.setPort(Integer.parseInt(environment.getProperty("mail.port")));
            sender.setUsername(environment.getProperty("mail.username"));
            sender.setPassword(environment.getProperty("mail.password"));

            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.auth", Boolean.TRUE);
            mailProperties.put("mail.smtp.starttls.enable", Boolean.TRUE);
            sender.setJavaMailProperties(mailProperties);

            return sender;
        }

        @Override
        public Environment getEnvironment() {
            return environment;
        }
    }

    @Configuration
    @Profile({"dev"})
    @PropertySource("classpath:META-INF/mail.dev.properties")
    class DevelopmentMailerProvider extends GenericMailerProvider {
    }

    @Configuration
    @Profile("88")
    @PropertySource("classpath:META-INF/mail.88.properties")
    class The88PropertyProvider extends GenericMailerProvider {
    }

    @Configuration
    @Profile("144")
    @PropertySource("classpath:META-INF/mail.144.properties")
    class The144PropertyProvider extends GenericMailerProvider {
    }

}
