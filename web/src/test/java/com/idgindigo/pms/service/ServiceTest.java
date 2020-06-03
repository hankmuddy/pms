package com.idgindigo.pms.service;

import com.idgindigo.pms.InitializingTest;
import com.idgindigo.pms.configuration.MailConfig;
import com.idgindigo.pms.configuration.SecurityConfiguration;
import com.idgindigo.pms.configuration.ServiceConfig;
import com.idgindigo.pms.configuration.TestJpaConfig;
import com.idgindigo.pms.configuration.TestLoginsJpaConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 10:54 AM
 */
@ContextConfiguration(classes = {TestJpaConfig.class, TestLoginsJpaConfig.class, ServiceConfig.class, SecurityConfiguration.class, MailConfig.class})
@ActiveProfiles("dev")
public class ServiceTest extends InitializingTest {
}
