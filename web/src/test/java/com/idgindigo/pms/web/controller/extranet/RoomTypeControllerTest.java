package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeDetails;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeDetailsRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.service.approve.RoomTypeApproveService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.SerializerUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeDetailsProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author vomel
 * @since 01.11.13 16:00
 */
public class RoomTypeControllerTest extends ApprovableControllerTest<RoomType> {
    @Inject
    private RoomTypeProvider provider;
    @Inject
    private RoomTypeDetailsProvider roomTypeDetailsProvider;
    @Inject
    private RoomTypeDetailsRepository roomTypeDetailsRepository;
    @Inject
    private RoomTypeController controller;
    @Tested
    private RoomTypeController mockController;
    @Injectable @Inject
    private RoomTypeRepository repository;
    @Injectable @Inject
    private BaseRoomRepository baseRoomRepository;
    @Injectable @Inject
    private LivingService livingService;
    @Injectable
    private RoomTypeApproveService approveService;

    @Override
    protected EntityProvider<RoomType> getProvider() {
        return provider;
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testUniqueShortname() {
        final RoomType rt1 = provider.getFirst();
        RoomType rt2 = provider.getTransientEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setShortname(rt1.getShortname());
            }
        });
        provider.getRepository().saveAndFlush(rt2);
    }

    /*@Override
    protected RoomType createEntity(Visitor<RoomType>... visitor) {
        return getRoomTypeDto();
    }

    @Test
    public void testDefaultVirtualRoomCreation() throws Exception {
        RoomType roomType = getRoomTypeDto();
        roomType = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(roomType)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RoomType.class);

        List<BaseRoom> baseRooms = baseRoomRepository.findByRoomType(roomType);
        Assert.assertEquals(baseRooms.size(), 1);
        BaseRoom baseRoom = baseRooms.get(0);
        Assert.assertEquals(baseRoom.roomType(), roomType);
        Assert.assertEquals(baseRoom.getAdditional(), roomType.getAdditional());
        Assert.assertEquals(baseRoom.getAdults(), roomType.getAdults());
        Assert.assertEquals(baseRoom.getChildren(), roomType.getChildren());
        Assert.assertEquals(baseRoom.getDefaultPrice(), roomType.getDefaultPrice());
        Assert.assertEquals(baseRoom.getApproved().booleanValue(), false);

    }

    private RoomTypeDto getRoomTypeDto(Visitor<RoomTypeDto>... visitor) {
        RoomType roomType = provider.getTransientEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setApproved(false);
            }
        });

        RoomTypeDto dto = new RoomTypeDto();
        BeanUtils.copyProperties(roomType, dto);
        BaseRoom vr = virtualRoomProvider.createAndFill();
        BeanUtils.copyProperties(roomType, vr, "shortname");
        dto.setBaseRoom(vr);
        for (Visitor<RoomTypeDto> rtv : visitor) {
            rtv.visit(dto);
        }
        return dto;
    }

    @Test
    public void testShortnameUniqueness() throws Exception {
        final RoomType rt = getRoomTypeDto();
        mvc.perform(preparePost(rt)).andExpect(status().isCreated());

        RoomType rt2 = getRoomTypeDto(new Visitor<RoomTypeDto>() {
            @Override
            public void visit(RoomTypeDto entity) {
                entity.setShortname(rt.getShortname());
            }
        });
        RoomType rt2vr = getRoomTypeDto(new Visitor<RoomTypeDto>() {
            @Override
            public void visit(RoomTypeDto entity) {
                entity.getBaseRoom().setShortname(rt.getShortname());
            }
        });
        RoomType rt3 = getRoomTypeDto(new Visitor<RoomTypeDto>() {
            @Override
            public void visit(RoomTypeDto entity) {
                entity.getBaseRoom().setShortname(entity.getShortname());
            }
        });
        testBadRequest(preparePost(rt3), RestFriendlyException.DUPLICATE_ENTRY);
        testBadRequest(preparePost(rt2), RestFriendlyException.DUPLICATE_ENTRY);
        testBadRequest(preparePost(rt2vr), RestFriendlyException.DUPLICATE_ENTRY);
    }
    */

    @Test
    public void testLivingHandling() throws Exception {
        final BaseRoom baseRoom = provider.getPersistentEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setApproved(false);
            }
        });
        new NonStrictExpectations(approveService) {{
            approveService.getApproveRepository();
            result = repository;
        }};
        mockController.approve(baseRoom.getId());
        new Verifications() {{
            livingService.handleCreate(baseRoom);
        }};
    }

    @Test
    public void testDetailsCascadeCreate() {
        RoomType roomType = provider.getTransientEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setDetails(roomTypeDetailsProvider.getTransientEntity());
            }
        });
        roomType = controller.create(roomType).getContent();
        roomType = repository.findOne(roomType.getId());
        assertNotNull(roomType.getDetails());
    }

    @Test
    public void testDetailsCascadeUpdate() {
        final RoomTypeDetails transientOriginalDetails = roomTypeDetailsProvider.getTransientEntity();
        final RoomType roomType = provider.getPersistentEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setApproved(false);
                entity.setDetails(transientOriginalDetails);
            }
        });
        RoomTypeDetails originalDetails = roomType.getDetails();

        //Test update with blank details
        RoomTypeDetails newDetails = roomTypeDetailsProvider.getTransientEntity();
        roomType.setDetails(newDetails);
        newDetails = controller.update(roomType, roomType.getId()).getContent().getDetails();
        assertNotEquals(roomTypeDetailsRepository.findOne(newDetails.getId()), originalDetails);

        //Test update with changed details
        assertEquals(newDetails.getSingle(), 0);
        roomType.setDetails(newDetails);
        newDetails.setSingle(10);
        newDetails = controller.update(roomType, roomType.getId()).getContent().getDetails();
        assertEquals(roomTypeDetailsRepository.findOne(newDetails.getId()).getSingle(), 10);
    }

    @Test
    public void testRoomTypeIsDefaulBaseRoom() {
        RoomType roomType = provider.getPersistentEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setApproved(false);
            }
        });
        Assert.assertFalse(roomType.getDefaultBaseRoom());

        controller.approve(roomType.getId());
        Assert.assertTrue(repository.findOne(roomType.getId()).getDefaultBaseRoom());
    }

    @Override
    protected String serialize(Object entity) throws Exception {
        return SerializerUtils.serializeWithTypeInfo(entity, objectMapper);
    }

    @Override
    protected String getUrl() {
        return RoomTypeController.URL + "/";
    }
}
