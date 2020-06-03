package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.repository.extranet.service.FixedServiceRepository;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 3:01 PM
 */
public class SimpleServiceControllerTest extends BaseWebCrudTest<SimpleService> {
    @Inject
    private SimpleServiceProvider provider;
    @Tested
    private SimpleServiceController controller;
    @Injectable
    @Inject
    private FixedServiceRepository fixedServiceRepository;
    @Injectable
    @Inject
    private SimpleServiceRepository repository;

    @Test
    public void testDuplicateTitle() {
        final SimpleService service = provider.getPersistentEntity();
        final SimpleService duplicate = provider.getTransientEntity(new Visitor<SimpleService>() {
            @Override
            public void visit(SimpleService entity) {
                entity.setTitle(service.getTitle());
            }
        });

        new DuplicateEntryTest() {
            @Override
            public void performRequest() {
                controller.create(duplicate);
            }
        }.testDuplicateEntry();

        final SimpleService duplicateUpdate = provider.getPersistentEntity();
        duplicateUpdate.setTitle(service.getTitle());

        new DuplicateEntryTest() {
            @Override
            public void performRequest() {
                controller.update(duplicateUpdate, duplicateUpdate.getId());
            }
        }.testDuplicateEntry();
    }

    private abstract class DuplicateEntryTest {
        public abstract void performRequest();

        public void testDuplicateEntry() {
            try {
                performRequest();
                throw new AssertionError("Should not happen");
            } catch (RestFriendlyException ex) {
                assertEquals(ex.getMessage(), RestFriendlyException.DUPLICATE_ENTRY);
                assertEquals(ex.getSource(), "title");
            }
        }
    }

    @Override
    protected EntityProvider<SimpleService> getProvider() {
        return provider;
    }

    @Override
    protected boolean doRunDeleteStep() {
        return false;
    }

    @Override
    protected String getUrl() {
        return SimpleServiceController.URL + "/";
    }
}
