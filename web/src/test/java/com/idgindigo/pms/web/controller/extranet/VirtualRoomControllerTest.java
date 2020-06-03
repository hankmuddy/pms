package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:39 PM
 */
public class VirtualRoomControllerTest extends ApprovableControllerTest<VirtualRoom> {
    @Inject
    private VirtualRoomProvider provider;

    @Inject
    private RoomTypeProvider roomTypeProvider;

    @Override
    protected EntityProvider<VirtualRoom> getProvider() {
        return provider;
    }

    @Mocked
    private Logger logger;

    @Test
    public void testRepeatedApproveWarning() throws Exception {
        Logger original = Deencapsulation.getField(VirtualRoomController.class, "logger");
        Deencapsulation.setField(VirtualRoomController.class, "logger", logger);

        final BaseRoom virtualRoom = provider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setName("VR 1");
                entity.setApproved(false);
            }
        });
        final BaseRoom virtualRoom2 = provider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setName("VR 2");
                entity.setApproved(false);
                entity.setRoomType(virtualRoom.roomType());
            }
        });
        Long id2 = virtualRoom.getId();
        Long id3 = virtualRoom2.getId();
        Assert.assertFalse(provider.getRepository().findOne(id3).getApproved());
        Assert.assertFalse(provider.getRepository().findOne(id2).getApproved());
        mvc.perform(preparePut(id3 + "," + id2 + "/approved")).andExpect(status().isOk());
        Assert.assertTrue(provider.getRepository().findOne(id3).getApproved());
        Assert.assertTrue(provider.getRepository().findOne(id2).getApproved());
        mvc.perform(preparePut(id3 + "," + id2 + "/approved")).andExpect(status().isOk());
        Assert.assertTrue(provider.getRepository().findOne(id3).getApproved());
        Assert.assertTrue(provider.getRepository().findOne(id2).getApproved());
        new Verifications() {{
            logger.warn("Could not approve already approved virtualRoom: {}", virtualRoom2);
            logger.warn("Could not approve already approved virtualRoom: {}", virtualRoom);
        }};
        Deencapsulation.setField(VirtualRoomController.class, "logger", original);
    }

    @Test
    public void testShortnameUniqueness() throws Exception {
        final VirtualRoom vr0 = provider.getPersistentEntity();
        VirtualRoom vr2 = provider.getTransientEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setShortname(vr0.getShortname());
            }
        });
        testBadRequest(preparePost(vr2), RestFriendlyException.DUPLICATE_ENTRY);
    }

    @Inject
    private VirtualRoomController virtualRoomController;
    @Mocked
    private LivingService livingService;

    @Test
    public void testLivingHandling() throws Exception {
        LivingService original = Deencapsulation.getField(virtualRoomController, "livingService");
        Deencapsulation.setField(virtualRoomController, "livingService", livingService);

        final BaseRoom baseRoom = provider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setApproved(false);
            }
        });

        mvc.perform(preparePut(baseRoom.getId() + "/approved")).andExpect(status().isOk());

        new Verifications() {{
            livingService.handleCreate(baseRoom);
        }};

        Deencapsulation.setField(virtualRoomController, "livingService", original);
    }

    @Override
    protected String getUrl() {
        return VirtualRoomController.URL + "/";
    }
}
