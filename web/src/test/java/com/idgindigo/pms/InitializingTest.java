package com.idgindigo.pms;

import com.idgindigo.pms.configuration.UtilsConfig;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.service.pms.SystemServiceService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/17/13 3:12 PM
 */
@ContextConfiguration(classes = UtilsConfig.class)
public abstract class InitializingTest extends AbstractTestNGSpringContextTests {
    @Inject
    private SimpleServiceProvider provider;
    private static boolean initialized;

    @BeforeSuite
    public void initializeServices() throws Exception {
        //create system services
        initServices();
    }

    private void initServices() throws Exception {
        if (!initialized) {
            initialized = true;
            springTestContextPrepareTestInstance();
            if (provider.getRepository().count() == 0) {
                createSystemService(SystemServiceService.ADULT_BED_SERVICE);
                createSystemService(SystemServiceService.CHILD_BED_SERVICE);
                createSystemService(SystemServiceService.EARLY_CHECK_IN);
                createSystemService(SystemServiceService.LATE_CHECK_OUT);
            }
        }
    }

    private void createSystemService(final String title) {
        provider.getPersistentEntity(new Visitor<SimpleService>() {
            @Override
            public void visit(SimpleService entity) {
                entity.setTitle(title);
                entity.setSystem(true);
            }
        });
    }

}