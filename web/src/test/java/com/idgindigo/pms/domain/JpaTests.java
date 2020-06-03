package com.idgindigo.pms.domain;

import com.idgindigo.pms.InitializingTest;
import com.idgindigo.pms.configuration.TestJpaConfig;
import com.idgindigo.pms.configuration.TestLoginsJpaConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author vomel
 * @since 29.10.13 14:58
 */
//@ContextConfiguration(classes = {PostgresDbTestJpaConfig.class, LoginsJpaConfig.class})
@ContextConfiguration(classes = {TestJpaConfig.class, TestLoginsJpaConfig.class})
@ActiveProfiles("dev")
public abstract class JpaTests extends InitializingTest {

}