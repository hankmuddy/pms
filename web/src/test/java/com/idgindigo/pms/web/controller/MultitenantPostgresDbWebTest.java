package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.configuration.MultitenantPostgresDbTestJpaConfig;
import com.idgindigo.pms.domain.BaseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author valentyn_vakatsiienko
 * @since 11/14/13 4:41 PM
 */
@ContextConfiguration(classes = {MultitenantPostgresDbTestJpaConfig.class})
public abstract class MultitenantPostgresDbWebTest<T extends BaseEntity> extends WebTest<T> {
}
