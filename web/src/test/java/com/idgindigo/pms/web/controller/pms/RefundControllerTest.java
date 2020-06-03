package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.restutils.exception.RefundException;
import com.idgindigo.pms.service.pms.RefundService;
import com.idgindigo.pms.utils.SerializerUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RefundProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import mockit.Mocked;
import mockit.Verifications;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 2:02 PM
 */
public class RefundControllerTest extends InMemoryDbWebTest<Refund> {
    @Inject
    private RefundProvider provider;
    @Inject
    private RefundController refundController;
    @Mocked
    private RefundService refundService;
    @Inject
    private SimpleServiceUseProvider serviceUseProvider;

    @Test
    public void testRefundCreate() {
        final Refund refund = provider.getTransientEntity();

        refundController.refund(refund);

        new Verifications() {{
            refundService.refund(refund);
        }};
    }

    @Test
    public void testEmptyServiceUses() throws Exception {
        final Refund refund = provider.getTransientEntity(new Visitor<Refund>() {
            @Override
            public void visit(Refund entity) {
                entity.setServiceUses(new ArrayList<BaseServiceUse>());
            }
        });

        testBadRequest(preparePost(refund), RefundException.SERVICE_USES_EMPTY);
    }

    @Test
    public void testWrongRoomUse() throws Exception {
        final Refund refund = provider.getTransientEntity(new Visitor<Refund>() {
            @Override
            public void visit(Refund entity) {
                entity.getServiceUses().add(serviceUseProvider.getPersistentEntity());
            }
        });

        testBadRequest(preparePost(refund), RefundException.INVALID_ROOM_USE);
    }

    @Override
    protected String serialize(Object entity) throws Exception {
        return SerializerUtils.serializeWithTypeInfo(entity, objectMapper);
    }

    @Override
    protected String getUrl() {
        return RefundController.URL + "/";
    }
}
