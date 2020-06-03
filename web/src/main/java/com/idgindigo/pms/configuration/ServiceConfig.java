package com.idgindigo.pms.configuration;

import com.idgindigo.pms.service.misc.PriceResolverService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 11:09 AM
 */
@Configuration
@ComponentScan(basePackages = {
        "com.idgindigo.pms.service.merchant",
        "com.idgindigo.pms.service.extranet",
        "com.idgindigo.pms.service.multitenancy",
        "com.idgindigo.pms.service.pms",
        "com.idgindigo.pms.service.admin",
        "com.idgindigo.pms.service.approve",
        "com.idgindigo.pms.service.channels",
        "com.idgindigo.pms.service.broadcast",
        "com.idgindigo.pms.price",
        "com.idgindigo.pms.service.validation",
})
@PropertySource(value = "classpath:META-INF/app.properties", name = "appProperties")
@EnableScheduling
public class ServiceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PriceResolverService priceResolverService() {
        return new PriceResolverService();
    }

}
