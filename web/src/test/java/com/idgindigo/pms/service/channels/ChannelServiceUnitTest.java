package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.channel.wubook.Wubook;
import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.VerificationsInOrder;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vomel
 * @since 01.04.14 16:22
 */
public class ChannelServiceUnitTest {

    @Mocked
    private Wubook wubook;

    @Test
    public void testFetchBookings() {
        new NonStrictExpectations() {{
            wubook.fetchNewBookings("supertoken");
            result = new Object[ChannelService.WUBOOK_MAX_OUTPUT];
            wubook.fetchBookings("supertoken", "01/12/2032", "01/12/2034", false);
            result = new Object[ChannelService.WUBOOK_MAX_OUTPUT];
        }};
        ChannelService.fetchBookings(wubook, "supertoken", new WubookAccount("a", "b", "c"), LocalDate.parse("01/12/2032", ChannelService.FORMATTER), LocalDate.parse("01/12/2034", ChannelService.FORMATTER), false, "mighty", "user");
        new VerificationsInOrder() {{
            wubook.fetchBookings("supertoken", "01/12/2032", "04/01/2033", false);
            wubook.fetchBookings("supertoken", "04/01/2033", "07/02/2033", false);
            wubook.fetchBookings("supertoken", "07/02/2033", "13/03/2033", false);
            wubook.fetchBookings("supertoken", "13/03/2033", "16/04/2033", false);
            wubook.fetchBookings("supertoken", "16/04/2033", "20/05/2033", false);
            wubook.fetchBookings("supertoken", "20/05/2033", "23/06/2033", false);
            wubook.fetchBookings("supertoken", "23/06/2033", "27/07/2033", false);
            wubook.fetchBookings("supertoken", "27/07/2033", "30/08/2033", false);
            wubook.fetchBookings("supertoken", "30/08/2033", "03/10/2033", false);
            wubook.fetchBookings("supertoken", "03/10/2033", "06/11/2033", false);
            wubook.fetchBookings("supertoken", "06/11/2033", "10/12/2033", false);
            wubook.fetchBookings("supertoken", "10/12/2033", "13/01/2034", false);
            wubook.fetchBookings("supertoken", "13/01/2034", "16/02/2034", false);
            wubook.fetchBookings("supertoken", "16/02/2034", "22/03/2034", false);
            wubook.fetchBookings("supertoken", "22/03/2034", "25/04/2034", false);
            wubook.fetchBookings("supertoken", "25/04/2034", "29/05/2034", false);
            wubook.fetchBookings("supertoken", "29/05/2034", "02/07/2034", false);
            wubook.fetchBookings("supertoken", "02/07/2034", "05/08/2034", false);
            wubook.fetchBookings("supertoken", "05/08/2034", "08/09/2034", false);
            wubook.fetchBookings("supertoken", "08/09/2034", "12/10/2034", false);
            wubook.fetchBookings("supertoken", "12/10/2034", "15/11/2034", false);
            wubook.fetchBookings("supertoken", "15/11/2034", "19/12/2034", false);
        }};
    }

    @Test(dataProvider = "data")//TODO expand this test with more data
    public void testSelectPlan(final String rateString, final String expected) {
        ChannelService channelService = new ChannelService();
        LivingRepository livingRepository = new MockUp<LivingRepository>() {
            @Mock
            List<Living> findAll(Specification<Living> spec) {
                return Arrays.asList(new Living() {{
                                         setPrices(13300L);
                                         setPlan(new Plan() {{
                                             setName("Best Plan HB");
                                             setBoard(Board.HB);
                                             setApproved(Boolean.TRUE);
                                         }});
                                     }}, new Living() {
                                         {
                                             setPrices(13400L);
                                             setPlan(new Plan() {{
                                                 setName("this is No board");
                                                 setBoard(Board.HB);
                                                 setApproved(Boolean.TRUE);
                                             }});
                                         }
                                     }
                );
            }

        }.getMockInstance();
        Deencapsulation.setField(channelService, "livingRepository", livingRepository);
        Map<String, Object> ancillary = new HashMap<String, Object>() {{
            put("Room (0) Economy Single Room", new HashMap<String, Object>() {{
                put("Daily rates", new Object[]{rateString});
            }});
        }};
        Plan selected = channelService.selectPlan(0, 0L, ancillary);
        if (expected != null) {
            Assert.assertEquals(selected.getName(), expected);
        } else {
            Assert.assertNull(selected);
        }
    }

    @DataProvider(name = "data")
    public Object[][] getData() {
        List<Object[]> result = new ArrayList<Object[]>(8);
        result.add(new Object[]{"2014-03-30: 155.00 (Half-Board Rate)", "Best Plan HB"});
        result.add(new Object[]{"2014-03-30: 155.00 (Standard Rate)", "this is No board"});
        result.add(new Object[]{"2014-03-30: 155.00 (Very Unusual Rate)", "this is No board"});
        return result.toArray(new Object[result.size()][]);
    }
}
