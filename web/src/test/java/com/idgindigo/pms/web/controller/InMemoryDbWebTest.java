package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.configuration.TestJpaConfig;
import com.idgindigo.pms.domain.BaseIdentifiable;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author vomel
 * @since 29.10.13 16:00
 */
@ContextConfiguration(classes = {TestJpaConfig.class})
public abstract class InMemoryDbWebTest<T extends BaseIdentifiable> extends WebTest<T> {
}
