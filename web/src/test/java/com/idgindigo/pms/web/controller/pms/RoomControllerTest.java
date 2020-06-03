package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 12:02 PM
 */
public class RoomControllerTest extends ApprovableControllerTest<Room> {

    @Inject
    private RoomController roomController;
    @Inject
    private SettingsService settingsService;
    @Inject
    private RoomRepository repository;
    @Mocked
    private ChannelService channelService;
    @Mocked
    private QuotaService quotaService;

    @Override
    public void testApproved() throws Exception {
        new NonStrictExpectations() {{
            channelService.newRoom(withAny(new RoomType()), withAny(new WubookAccount(null, null, null)));
            result = 1L;
            channelService.modRoom(withAny(new RoomType()), withAny(new WubookAccount(null, null, null)));
            result = 1L;
        }};
        super.testApproved();
    }

    @Test
    public void testInitialPositionSet() throws Exception {
        Room room = provider.getTransientEntity();
        Room created = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(room)).andReturn().getResponse().getContentAsString(), Room.class);
        assertEquals(created.getId() <= Integer.MAX_VALUE ? created.getId().intValue() : Integer.MAX_VALUE, created.getPosition());
    }

    @Test
    public void testQuotaManagement() {
        final Room room = getNonApproved();

        roomController.approve(room.getId());

        new Verifications() {{
            quotaService.updateQuota(settingsService.getHotelDate(), room.getRoomType(), 1);
        }};
    }

    @Test
    public void testExceedMaxRooms() {
        Room room = getNonApproved();

        new NonStrictExpectations(settingsService) {{
            settingsService.getMaxRooms();
            result = repository.findByApprovedTrue().size();
        }};

        try {
            roomController.approve(room.getId());
            fail();
        } catch (RestFriendlyException e) {
            assertEquals(e.getMessage(), RestFriendlyException.MAX_ROOMS_EXCEEDED);
        }
    }

    @Test
    public void testApproveWithRoomLimit() {
        Room room = getNonApproved();

        new NonStrictExpectations(settingsService) {{
            settingsService.getMaxRooms();
            result = repository.findByApprovedTrue().size() + 1;
        }};

        roomController.approve(room.getId());
        assertTrue(repository.isApproved(room.getId()));
    }

    @Test
    public void testSetPosition() {
        Map<Integer, Room> map = new HashMap<>();
        for (int pos = 1; pos <= 5; pos++) {
            map.put(pos, getRoom(pos));
        }

        Room first = map.get(1);
        roomController.setPosition(first.getId(), 5);
        checkPositionUpdate(map, first, 1, 5);

        for (Integer pos : map.keySet()) {
            map.put(pos, repository.findOne(map.get(pos).getId()));
        }

        roomController.setPosition(first.getId(), 1);
        checkPositionUpdate(map, first, 5, 1);
    }

    private void checkPositionUpdate(Map<Integer, Room> map, Room toUpdate, int oldPos, int newPos) {
        for (Integer pos : map.keySet()) {
            Room room = map.get(pos);
            Room updated = repository.findOne(room.getId());
            if (room.equals(toUpdate)) {
                assertEquals(updated.getPosition(), newPos > oldPos ? 5 : 1);
            } else {
                assertEquals(updated.getPosition(), room.getPosition() + (newPos > oldPos ? -1 : 1));
            }
        }
    }

    private Room getRoom(final int position) {
        return provider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setPosition(position);
            }
        });
    }

    public Room getNonApproved() {
        return provider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setApproved(false);
            }
        });
    }

    @Inject
    private RoomProvider provider;

    @Override
    protected EntityProvider<Room> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return RoomController.URL + "/";
    }

    @Override
    protected boolean isModificationAllowed() {
        return true;
    }
}
