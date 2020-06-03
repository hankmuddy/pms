package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.axmlrpc.ResponseParser;
import com.idgindigo.pms.axmlrpc.XmlRpcClient;
import com.idgindigo.pms.axmlrpc.serializer.SerializerHandler;
import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.plan.BasePlan;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.extranet.plan.BasePlanRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.ServiceTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author vomel
 * @since 17.03.14 15:52
 */
public class ChannelServiceTest extends ServiceTest {
    @Inject
    private ChannelService channelService;
    @Inject
    private AdultRepository adultRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private BasePlanRepository basePlanRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceTest.class);

    private static final String WUBOOK_ACCOUNT = "wubook.account.properties";
    public static final WubookAccount ACCOUNT;

    static {
        Properties settings = new Properties();
        InputStream is = WubookImpl.class.getClassLoader().getResourceAsStream(WUBOOK_ACCOUNT);
        try {
            settings.load(is);
            logger.debug("settings = {}", settings);
            ACCOUNT = new WubookAccount(settings.getProperty("wubook.username"), settings.getProperty("wubook.password"), settings.getProperty("wubook.lcode"));
        } catch (IOException e) {
            throw new AssertionError("Could not load data from: " + WUBOOK_ACCOUNT, e);
        }
        SerializerHandler.initialize(XmlRpcClient.FLAGS_NONE);
    }

    private Object[] getFromWubook(String data) {
        Object[] response;
        try {
            Object toParse = new ResponseParser().parse(new ByteArrayInputStream(data.getBytes()));
            response = (Object[]) toParse;
        } catch (Exception e) {
            logger.debug("data =" + data);
            throw new RuntimeException(e);
        }
        return response;
    }

    @Test
    public void testOstrovokContact() {
        int initialSize = adultRepository.findAll().size();
        String email = "Malformed email";
        Adult adult0 = channelService.parseCustomer(getBooking("A last name", "normal@email.com"));
        Assert.assertEquals(adultRepository.findAll().size(), initialSize + 1);
        Adult adult1 = channelService.parseCustomer(getBooking("A last name", email));
        Assert.assertEquals(adultRepository.findAll().size(), initialSize + 2);
        Adult adult2 = channelService.parseCustomer(getBooking("A different last name", email));
        Assert.assertEquals(adultRepository.findAll().size(), initialSize + 3);
        Assert.assertEquals(adult0.getEmail(), "normal@email.com");
        Assert.assertEquals(adultRepository.findOne(adult1.getId()).getEmail(), null);
        Assert.assertEquals(adultRepository.findOne(adult2.getId()).getEmail(), null);
    }

    public static HashMap<String, Object> getBooking(final String lastName, final String email) {
        return new HashMap<String, Object>() {{
            put("customer_name", "A first name");
            put("customer_surname", lastName);
            put("customer_phone", "A phone");
            put("customer_mail", email);
        }};
    }

    @Test//TODO Use MockWubook answer
    public void testFetchPlans() {
        if (!SecurityUtils.isWubookConfigured() || !WubookImpl.ENABLED) {
            return;
        }
        Collection<BasePlan> plans = channelService.fetchPlans(ACCOUNT);
        basePlanRepository.save(plans);
    }

    @Test//TODO Use MockWubook answer
    public void testFetchRooms() throws Exception {
        if (!SecurityUtils.isWubookConfigured() || !WubookImpl.ENABLED) {
            return;
        }
        int initialSizeRt = roomTypeRepository.findAll().size();
        int initialSizeVr = baseRoomRepository.findAll().size();
        Collection<RoomType> roomTypes = channelService.fetchRooms(ACCOUNT);
        int newVrs = 0;
        for (RoomType roomType : roomTypes) {
            List<VirtualRoom> baseRooms = roomType.getVirtualRooms();
            newVrs += baseRooms == null ? 0 : baseRooms.size();
        }
        Assert.assertFalse(roomTypes.isEmpty());
        Assert.assertEquals(roomTypeRepository.findAll().size(), initialSizeRt + roomTypes.size());
        Assert.assertEquals(baseRoomRepository.findAll().size(), initialSizeVr + newVrs);
    }

}
