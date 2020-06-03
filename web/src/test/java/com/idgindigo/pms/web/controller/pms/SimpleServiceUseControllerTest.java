package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.price.SimpleServicePriceResolver;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.exception.ServiceUseException;
import com.idgindigo.pms.service.misc.PriceResolverService;
import com.idgindigo.pms.service.pms.BillService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.SerializerUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 3:03 PM
 */
public class SimpleServiceUseControllerTest extends BaseWebCrudTest<SimpleServiceUse> {
    @Inject
    private SimpleServiceUseProvider provider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private SimpleServiceUseController controller;
    @Inject
    private SimpleServicePriceResolver priceResolver;
    @Tested
    private SimpleServiceUseController mockController;
    @Injectable
    @Inject
    private SimpleServiceRepository serviceRepository;
    @Injectable
    private BillService billService;
    @Injectable
    @Inject
    private BillRepository billRepository;
    @Injectable
    private PriceResolverService priceResolverService;
    @Injectable
    @Inject
    private SimpleServiceUseRepository repository;
    @Injectable
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;

    @Test(dataProvider = "testInvalidBill")
    public void testInvalidBill(final SimpleServiceUse simpleServiceUse) {
        new InvalidBillTest() {
            @Override
            public void performRequest() {
                mockController.create(simpleServiceUse);
            }
        }.testInvalidBill();
    }

    @DataProvider(name = "testInvalidBill")
    public Object[][] getData_testInvalidBill() {
        List<Object[]> result = new ArrayList<>(8);

        result.add(new Object[]{getWithBill(null)});
        return result.toArray(new Object[result.size()][]);
    }

    private abstract class InvalidBillTest {
        public abstract void performRequest();

        public void testInvalidBill() {
            try {
                performRequest();
                throw new AssertionError("Should not happen");
            } catch (RestFriendlyException ex) {
                assertEquals(ex.getMessage(), ServiceUseException.INVALID_BILL);
                assertEquals(ex.getSource(), "bill");
            }
        }
    }

    @Test
    public void testBillCascadeCreate() {
        SimpleServiceUse serviceUse = getWithBill(billProvider.getTransientEntity());
        serviceUse = controller.create(serviceUse).getContent();
        Bill bill = getProvider().getRepository().findOne(serviceUse.getId()).getBill();
        assertNotNull(bill);
        assertNotNull(bill.getId());
    }

    @Injectable
    @Inject
    private RoomUseRepository roomUseRepository;

    @Test
    public void testCreateTotalManagement() {
        final SimpleServiceUse serviceUse = provider.getTransientEntity();
        new NonStrictExpectations(priceResolverService) {{
            priceResolverService.getPriceResolver(serviceUse.getService());
            result = priceResolver;
        }};

        mockController.create(serviceUse);

        new Verifications() {{
            priceResolverService.getPriceResolver(serviceUse.getService());
        }};
        verifyUpdateTotal(serviceUse);
    }

    @Test
    public void testUpdateTotalManagement() {
        final SimpleServiceUse serviceUse = provider.getPersistentEntity();
        mockController.update(serviceUse, serviceUse.getId());

        verifyUpdateTotal(serviceUse);
    }

    @Test
    public void testDeleteTotalManagement() {
        final SimpleServiceUse serviceUse = provider.getPersistentEntity();
        mockController.delete(serviceUse.getId());

        verifyUpdateTotal(serviceUse);
    }

    private void verifyUpdateTotal(final SimpleServiceUse serviceUse) {
        new Verifications() {{
            billService.updateTotal(serviceUse.getBill(), null);
        }};
    }

    private SimpleServiceUse getWithBill(final Bill bill) {
        return provider.getTransientEntity(new Visitor<SimpleServiceUse>() {
            @Override
            public void visit(SimpleServiceUse entity) {
                entity.setBill(bill);
            }
        });
    }

    @Override
    protected EntityProvider<SimpleServiceUse> getProvider() {
        return provider;
    }

    @Override
    protected String serialize(Object entity) throws Exception {
        return SerializerUtils.serializeWithTypeInfo(entity, objectMapper);
    }

    @Test
    public void testEmptyRawTotal() throws Exception {
        SimpleServiceUse serviceUse = provider.getTransientEntity(new Visitor<SimpleServiceUse>() {
            @Override
            public void visit(SimpleServiceUse entity) {
                entity.setRawTotal(null);
            }
        });
        mvc.perform(preparePost(serviceUse)).andExpect(status().isCreated());
    }

    @Override
    protected String getUrl() {
        return SimpleServiceUseController.URL + "/";
    }
}
