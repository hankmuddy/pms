package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/6/14 1:52 PM
 */
public class BaseRoomControllerTest extends InMemoryDbWebTest {

    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private BaseRoomRepository repository;
    @Inject
    private BaseRoomController controller;

    @Test
    public void testUniqueDefaultBaseRoom() throws Exception {
        final RoomType one = roomTypeProvider.getPersistentEntity();
        BaseRoom two = virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setRoomType(one);
            }
        });
        Assert.assertNotEquals(repository.getDefault(one.getId()), one);
        Assert.assertNotEquals(repository.getDefault(one.getId()), two);

        controller.setDefault(one.getId());
        Assert.assertEquals(repository.getDefault(one.getId()), one);
        Assert.assertNotEquals(repository.getDefault(one.getId()), two);

        controller.setDefault(two.getId());
        Assert.assertNotEquals(repository.getDefault(one.getId()), one);
        Assert.assertEquals(repository.getDefault(one.getId()), two);
    }

    @Override
    protected String getUrl() {
        return BaseRoomController.URL + "/";
    }
}
