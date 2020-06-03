package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeFacilityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * @author vomel
 * @since 01.11.13 15:56
 */
public class RoomTypeTest extends ApprovableEntityTest<RoomType> {
    @Inject
    private RoomTypeProvider provider;
    @Inject
    private RoomTypeFacilityProvider rtfProvider;
    @Inject
    private RoomTypeToFacilityRepository roomTypeToFacilityRepository;

    @Test
    public void testWithFacility() {
        RoomType rt = provider.getPersistentEntity();
        RoomTypeFacility rtf1 = rtfProvider.getPersistentEntity();
        RoomTypeFacility rtf2 = rtfProvider.getPersistentEntity();
        RoomTypeFacility rtf3 = rtfProvider.getPersistentEntity();
        roomTypeToFacilityRepository.saveAndFlush(new RoomTypeToFacility(rt, rtf1));
        roomTypeToFacilityRepository.saveAndFlush(new RoomTypeToFacility(rt, rtf2));
        roomTypeToFacilityRepository.saveAndFlush(new RoomTypeToFacility(rt, rtf3));
        List<RoomTypeFacility> rtfs = roomTypeToFacilityRepository.findByRoomType(rt.getId());
        Assert.assertEquals(rtfs.size(), 3);
        List<RoomTypeFacility> expected = Arrays.asList(rtf1, rtf2, rtf3);
        for (RoomTypeFacility rtf : rtfs) {
            Assert.assertTrue(expected.contains(rtf));
        }
    }

    @Override
    protected EntityProvider<RoomType> getProvider() {
        return provider;
    }
}
