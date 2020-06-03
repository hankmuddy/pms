package com.idgindigo.pms.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author vomel
 * @since 29.10.13 16:02
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.idgindigo.pms.web")
public class TestWebConfig extends WebConfiguration {

}
