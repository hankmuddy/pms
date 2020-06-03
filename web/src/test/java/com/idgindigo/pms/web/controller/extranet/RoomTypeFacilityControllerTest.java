package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.utils.extranet.RoomTypeFacilityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.web.controller.extranet.RoomTypeFacilityController.RoomTypeFacilityDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * @author vomel
 * @since 19.02.14 14:41
 */
public class RoomTypeFacilityControllerTest extends InMemoryDbWebTest<RoomTypeFacility> {

    @Inject
    private RoomTypeFacilityProvider provider;
    @Inject
    private RoomTypeProvider rtProvider;
    @Inject
    private RoomTypeToFacilityRepository rttfRepository;

    @BeforeMethod
    public void beforeMethod() {
        rttfRepository.deleteAll();
    }

    @Test
    public void testFindFacilities() throws Exception {
        RoomType rt1 = rtProvider.getFirst();
        RoomType rt2 = rtProvider.getByIndex(1);
        RoomTypeFacility rtf1 = provider.getFirst();
        RoomTypeFacility rtf2 = provider.getByIndex(1);
        RoomTypeFacility rtf3 = provider.getByIndex(2);
        RoomTypeFacility rtf4 = provider.getByIndex(3);
        RoomTypeFacility rtf5 = provider.getByIndex(4);
        rttfRepository.saveAndFlush(new RoomTypeToFacility(rt1, rtf1));
        rttfRepository.saveAndFlush(new RoomTypeToFacility(rt1, rtf2));
        rttfRepository.saveAndFlush(new RoomTypeToFacility(rt1, rtf3));

        assertFacilities("byRoomType/" + rt1.getId(), Arrays.asList(rtf1, rtf2, rtf3));
        assertFacilities("byRoomType/" + rt2.getId(), Collections.emptyList());
        assertFacilities("", Arrays.asList(rtf1, rtf2, rtf3, rtf4, rtf5));
    }

    @Test
    public void testSaveFacilities() throws Exception {
        RoomType rt1 = rtProvider.getFirst();
        RoomType rt2 = rtProvider.getByIndex(1);
        RoomTypeFacility rtf1 = provider.getFirst();
        RoomTypeFacility rtf2 = provider.getByIndex(1);
        RoomTypeFacility rtf3 = provider.getByIndex(2);
        RoomTypeFacility rtf4 = provider.getByIndex(3);
        RoomTypeFacility rtf5 = provider.getByIndex(4);
        doPut(rt1, rtf1, rtf2, rtf3);
        doPut(rt2, rtf3, rtf4, rtf5);
        assertFacilities(rttfRepository.findByRoomType(rt1.getId()), rtf1, rtf2, rtf3);
        assertFacilities(rttfRepository.findByRoomType(rt2.getId()), rtf3, rtf4, rtf5);
        doPut(rt1, rtf1, rtf3, rtf5);
        doPut(rt2, rtf2, rtf4);
        assertFacilities(rttfRepository.findByRoomType(rt1.getId()), rtf1, rtf3, rtf5);
        assertFacilities(rttfRepository.findByRoomType(rt2.getId()), rtf2, rtf4);
    }

    private void doPut(RoomType roomType, RoomTypeFacility... facilities) throws Exception {
        Assert.assertEquals(mvc.perform(
                buildRequest(put(getUrl() + "bind"), new RoomTypeFacilityDto(new RoomType(roomType.getId()), Arrays.asList(facilities)))
        ).andReturn().getResponse().getStatus(), HttpStatus.CREATED.value());
    }

    @Test(enabled = false)
    public void testForbiddenActions() throws Exception {
        RoomType rt1 = rtProvider.getFirst();
        rt1.setRooms(null);
        rt1.setVirtualRooms(null);
        testBadRequest(preparePost(rt1), "Status");
        testNotFound(preparePut(rt1));
        testNotFound(prepareDelete(rt1.getId()));
    }

    private void assertFacilities(String url, Collection<?> facilities) throws Exception {
        List<RoomTypeFacility> result = getEntityList(url, RoomTypeFacility.class);
        assertFacilities(result, facilities.toArray(new RoomTypeFacility[facilities.size()]));
    }

    private static void assertFacilities(List<RoomTypeFacility> result, RoomTypeFacility... facilities) {
//        Assert.assertEquals(result.size(), facilities.length);
        Assert.assertTrue(result.containsAll(Arrays.asList(facilities)));
    }

    @Override
    protected String getUrl() {
        return RoomTypeFacilityController.URL + "/";
    }
}
