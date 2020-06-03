package com.idgindigo.pms.channel.wubook;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * @author vomel
 * @since 17.12.13 15:06
 */
public class WubookImplTest {
    private static final Logger logger = LoggerFactory.getLogger(WubookImplTest.class);
    private static final String WUBOOK_ACCOUNT = "wubook.account.properties";
    public static final WubookAccount ACCOUNT;
    public static final String WUBOOK_DATE_PATTERN = "dd/MM/yyyy";

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
    }

    @Test
    public void testFetchRooms() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                List<Map<String, Object>> maps = wubook.fetchRooms(token);
                new Printer().print(maps);
                return null;
            }
        }.call();
    }

    @Test(expectedExceptions = WubookException.class, expectedExceptionsMessageRegExp = "(?s).*Shortname has to be unique. This shortname is already used.*")
    public void testConstraints() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                long rid = -1L;
                try {
                    String shortname = string();
                    rid = wubook.newRoom(token, "Шроглі", 16, 110.0, 14, shortname, "NB");
                    long rid1of5 = wubook.newVirtualRoom(token, rid, "1of5", 5, 0, 100.0, shortname, "BB");
                } finally {
                    wubook.delRoom(token, rid);
                }
                return null;
            }
        }.call();
    }

    @Test(enabled = false)//TODO Investigate why this test fails
    public void testUpdatePlan() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                long rid = -1l;
//                long pidBb = -1l;
//                long pidFb = -1l;
                long pidAi = -1l;
                try {
                    String shortname = string();
                    //create room
                    rid = wubook.newRoom(token, "Шмаглі", 5, 111.0, 1, shortname, "BB");
                    logger.trace("rid = " + rid);
//                    long rid1of5 = wubook.newVirtualRoom(token, rid, "1of5", 5, 0, 100.0, string(), "BB");
//                    logger.trace("rid1of5 = " + rid1of5);
                    long rid3of5 = wubook.newVirtualRoom(token, rid, "3of5", 5, 0, 125.0, string(), "BB");
                    logger.trace("rid3of5 = " + rid3of5);
//                    long rid5of5 = wubook.newVirtualRoom(token, rid, "5of5", 5, 0, 150.0, string(), "BB");
//                    logger.trace("rid5of5 = " + rid5of5);

                    //create plan
//                    pidBb = wubook.addPlan(token, "Шмаглі with BB");
//                    pidFb = wubook.addPlan(token, "Шмаглі with FB");
                    pidAi = wubook.addPlan(token, "Шмаглі with AI");

//                    Object[] plansBb = wubook.getPlans(token, pidBb);
//                    assertPlan(rid, 111.0, plansBb);
//                    assertPlan(rid, 111.0, plansBb);
//                    assertPlan(rid, 111.0, plansBb);
//                    assertPlan(rid1of5, 100.0, plansBb);
//                    assertPlan(rid3of5, 125.0, plansBb);
//                    assertPlan(rid5of5, 150.0, plansBb);
//                    Object[] plansFb = wubook.getPlans(token, pidFb);
//                    assertPlan(rid1of5, 100.0, plansFb);
//                    assertPlan(rid3of5, 125.0, plansFb);
//                    assertPlan(rid5of5, 150.0, plansFb);
                    Object[] plansAi = wubook.getPlans(token, pidAi);
//                    assertPlan(rid1of5, 100.0, plansAi);
                    assertPlan(rid3of5, 125.0, plansAi);
//                    assertPlan(rid5of5, 150.0, plansAi);

                    //upload plan prices for 2 weeks
                    //update_plan_periods and check that other dates are not changed

                    wubook.updatePlanPeriods(token, pidAi, createPeriods(rid3of5, new LocalDate().plusDays(1), new LocalDate().plusDays(1)));

//                    Object[] plansBb2 = wubook.getPlans(token, pidBb);
//                    assertPlan(rid, 111.0, plansBb2);
//                    assertPlan(rid, 111.0, plansBb2);
//                    assertPlan(rid, 111.0, plansBb2);
//                    assertPlan(rid1of5, 100.0, plansBb2);
//                    assertPlan(rid3of5, 125.0, plansBb2);
//                    assertPlan(rid5of5, 150.0, plansBb2);
//                    Object[] plansFb2 = wubook.getPlans(token, pidFb);
//                    assertPlan(rid1of5, 100.0, plansFb2);
//                    assertPlan(rid3of5, 125.0, plansFb2);
//                    assertPlan(rid5of5, 150.0, plansFb2);
                    Object[] plansAi2 = wubook.getPlans(token, pidAi);
//                    assertPlan(rid1of5, 100.0, plansAi2);
                    assertPlan(rid3of5, 125.0, plansAi2);
                    assertPeriods(rid3of5, plansAi2);
//                    assertPlan(rid5of5, 150.0, plansAi2);
                } finally {
                    wubook.delRoom(token, rid);
//                    wubook.delPlan(token, pidBb);
//                    wubook.delPlan(token, pidFb);
                    wubook.delPlan(token, pidAi);
                }
                return null;
            }
        }.call();
    }

    private static Map<String, Object>[] createPeriods(final long rid, final LocalDate dfrom, final LocalDate dto) {
        Map<String, Object>[] periods = new Map[]{
                new HashMap<String, Object>() {{
                    //'dfrom': '21/12/2013', 'dto': '23/12/2013', 'values': {'1234': [1,2,3,4,5,6,7]}
                    put("dfrom", dfrom.toString(WUBOOK_DATE_PATTERN));
                    put("dto", dto.toString(WUBOOK_DATE_PATTERN));
                    put("values", new HashMap<String, Double[]>() {{
                        put(String.valueOf(rid), new Double[]{11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0});
                    }});
                }}
        };
        return periods;
    }

    @Test
    public void testPlanCrud() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                long rid = -1l;
                long pidBb = -1l;
                long pidFb = -1l;
                long pidAi = -1l;
                try {
                    String shortname = string();
                    rid = wubook.newRoom(token, "Шмаглі", 5, 111.0, 1, shortname, "BB");
                    logger.trace("rid = " + rid);
                    long rid1of5 = wubook.newVirtualRoom(token, rid, "1of5", 5, 0, 100.0, string(), "BB");
                    logger.trace("rid1of5 = " + rid1of5);
                    long rid3of5 = wubook.newVirtualRoom(token, rid, "3of5", 5, 0, 125.0, string(), "BB");
                    logger.trace("rid3of5 = " + rid3of5);
                    long rid5of5 = wubook.newVirtualRoom(token, rid, "5of5", 5, 0, 150.0, string(), "BB");
                    logger.trace("rid5of5 = " + rid5of5);

                    pidBb = wubook.addPlan(token, "Шмаглі with BB");
                    pidFb = wubook.addPlan(token, "Шмаглі with FB");
                    pidAi = wubook.addPlan(token, "Шмаглі with AI");

                    Object[] plansBb = wubook.getPlans(token, pidBb);
                    assertPlan(rid, 111.0, plansBb);
                    assertPlan(rid, 111.0, plansBb);
                    assertPlan(rid, 111.0, plansBb);
                    assertPlan(rid1of5, 100.0, plansBb);
                    assertPlan(rid3of5, 125.0, plansBb);
                    assertPlan(rid5of5, 150.0, plansBb);
                    Object[] plansFb = wubook.getPlans(token, pidFb);
                    assertPlan(rid1of5, 100.0, plansFb);
                    assertPlan(rid3of5, 125.0, plansFb);
                    assertPlan(rid5of5, 150.0, plansFb);
                    Object[] plansAi = wubook.getPlans(token, pidAi);
                    assertPlan(rid1of5, 100.0, plansAi);
                    assertPlan(rid3of5, 125.0, plansAi);
                    assertPlan(rid5of5, 150.0, plansAi);

                    wubook.updatePlanRack(token, pidBb, createRack(rid1of5, 200.0));
                    wubook.updatePlanRack(token, pidFb, createRack(rid3of5, 300.0));
                    wubook.updatePlanRack(token, pidAi, createRack(rid5of5, 400.0));

                    Object[] plans2Bb = wubook.getPlans(token, pidBb);
                    assertPlan(rid, 111.0, plans2Bb);
                    assertPlan(rid, 111.0, plans2Bb);
                    assertPlan(rid, 111.0, plans2Bb);
                    assertPlan(rid1of5, 200.0, plans2Bb);
                    assertPlan(rid3of5, 125.0, plans2Bb);
                    assertPlan(rid5of5, 150.0, plans2Bb);
                    Object[] plans2Fb = wubook.getPlans(token, pidFb);
                    assertPlan(rid1of5, 100.0, plans2Fb);
                    assertPlan(rid3of5, 300.0, plans2Fb);
                    assertPlan(rid5of5, 150.0, plans2Fb);
                    Object[] plans2Ai = wubook.getPlans(token, pidAi);
                    assertPlan(rid1of5, 100.0, plans2Ai);
                    assertPlan(rid3of5, 125.0, plans2Ai);
                    assertPlan(rid5of5, 400.0, plans2Ai);
                } finally {
                    wubook.delRoom(token, rid);
                    wubook.delPlan(token, pidBb);
                    wubook.delPlan(token, pidFb);
                    wubook.delPlan(token, pidAi);
                }
                return null;
            }
        }.call();
    }

    private static void assertPeriods(long rid3of5, Object[] periods) {
        Assert.assertEquals(periods.length, 1);
        Map<String, Object> period = (Map<String, Object>) periods[0];
        Object[] planPeriods = (Object[]) period.get("periods");
        Assert.assertEquals(planPeriods.length, 1);
        Map<String, Object> planPeriod = (Map<String, Object>) planPeriods[0];
        Map<String, Object[]> values = (Map<String, Object[]>) planPeriod.get("values");
        Object[] planPeriodPrices = values.get(String.valueOf(rid3of5));
        Assert.assertEquals(planPeriodPrices.length, 7);
        Assert.assertEquals(planPeriodPrices[0], 11.0);
        Assert.assertEquals(planPeriodPrices[1], 12.0);
        Assert.assertEquals(planPeriodPrices[2], 13.0);
        Assert.assertEquals(planPeriodPrices[3], 14.0);
        Assert.assertEquals(planPeriodPrices[4], 15.0);
        Assert.assertEquals(planPeriodPrices[5], 16.0);
        Assert.assertEquals(planPeriodPrices[6], 17.0);
        for (Object planPeriodPrice : planPeriodPrices) {
            logger.trace("planPeriodPrice = " + planPeriodPrice);
        }
    }

    private static void assertPlan(long rid, double expected, Object[] plans) {
        Assert.assertEquals(plans.length, 1);
        Map<String, Object> plan = (Map<String, Object>) plans[0];
        Map<String, Object[]> rack = (Map<String, Object[]>) plan.get("rack");
        Object[] rackPrices = rack.get(String.valueOf(rid));
        Assert.assertEquals(rackPrices.length, 7);
        for (Object rackPrice : rackPrices) {
            Assert.assertEquals(rackPrice, expected);
        }
    }

    private static Map<String, Object[]> createRack(long rid, double price) {
        Map<String, Object[]> map = new HashMap<>();
        map.put(String.valueOf(rid), new Object[]{price, price, price, price, price, price, price});
        return map;
    }

    private static String string() {
        return RandomStringUtils.random(4, true, true);
    }

    @Test(enabled = false)
    public void testRoomCrud() throws Exception {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                int originalSize = wubook.fetchRooms(token).size();
                new Printer().print(wubook.fetchRooms(token));
                String shortname = string();
                long rid = wubook.newRoom(token, "Книглі", 6, 10.0, 4, shortname, "NB");
                assertRoomFound(originalSize + 1, shortname, rid, wubook.fetchRooms(token), "Книглі", 6, 0);
                String vshortname = string();
                long vrid = wubook.newVirtualRoom(token, rid, "Книглі зі сніданком", 4, 2, 12.0, vshortname, "FB");
                assertRoomFound(originalSize + 2, vshortname, vrid, wubook.fetchRooms(token), "Книглі зі сніданком", 4, 2);
                String shortname2 = RandomStringUtils.random(4, true, false);
                String vshortname2 = RandomStringUtils.random(4, true, false);
                wubook.modRoom(token, rid, "Прєзідєнт Suite", 7, 9, 5, shortname2, "FB");
                assertRoomFound(originalSize + 2, shortname2, rid, wubook.fetchRooms(token), "Прєзідєнт Suite", 7, 0);
                wubook.modVirtualRoom(token, vrid, "Прєзідєнт Suite зі сніданком", 3, 2, 11.0, vshortname2, "HB");
                assertRoomFound(originalSize + 2, vshortname2, vrid, wubook.fetchRooms(token), "Прєзідєнт Suite зі сніданком", 3, 2);

                wubook.delRoom(token, rid);
                Assert.assertEquals(wubook.fetchRooms(token).size(), originalSize);
                return null;
            }
        }.call();
    }

    @Test
    public void testGetToken() throws Exception {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                return null;
            }
        }.call();
    }

    @Test
    public void testGetChannelSymbols() throws Exception {
        new WubookCaller<Object>(ACCOUNT) {
            @Override
            protected Object run(Wubook wubook, String token) {
                Map<String, Object[]> symbols = wubook.getChannelSymbols(token);
                Map<String, Object[]> sorted = new TreeMap<String, Object[]>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                    }
                });
                sorted.putAll(symbols);
                for (Map.Entry<String, Object[]> entry : sorted.entrySet()) {
                    String value = getProviderCode(entry.getValue()[0]);
                    System.out.println("            case " + entry.getKey() + ":\n" +
                            "                return " + value + ";//" + entry.getValue()[0]);
                }
                for (Map.Entry<String, Object[]> entry : sorted.entrySet()) {
                    String value = getProviderCode(entry.getValue()[0]);
                    System.out.print(value + ",");
                }
//                new Printer().print(sorted);
                return symbols;
            }
        }.call();
    }

    private static String getProviderCode(Object obj) {
        return String.valueOf(obj).toUpperCase().replaceAll(" ", "_").replaceAll("\\.", "_").replaceAll("\\-", "_").replace("(", "").replace(")", "");
    }

    @Test(enabled = false)
    public void testFetchCc() throws Exception {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Map<String, Object> wubooks = wubook.fetchCc(token, "xxxxxxxxxx", "xxxxxxxx");
                return null;
            }
        }.call();
    }

    @Test
    public void testFetchRoomsValues() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                LocalDate start = new LocalDate().plusWeeks(1);
                LocalDate end = start;
                Object o = wubook.fetchRoomsValues(token, start.toString(WUBOOK_DATE_PATTERN), end.toString(WUBOOK_DATE_PATTERN));

                Map<String, Object> o1 = (Map<String, Object>) o;
                if (logger.isTraceEnabled()) {
                    logger.trace("Price Values for: " + start.toString(WUBOOK_DATE_PATTERN) + " - " + end.toString(WUBOOK_DATE_PATTERN));
                    new Printer().print(o1);
                }
                return null;
            }
        }.call();
    }

    @Test(enabled = false)
    public void testUpdateRoomsValues() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                LocalDate start = new LocalDate().plusWeeks(1);
                LocalDate end = start.plusDays(1);

                //Let's pick an arbitrary room
                final Long rid = getArbitraryRoom(wubook, token);

                //Remember original values
                Map<String, Object[]> originalRoomsValues = wubook.fetchRoomsValues(token, start.toString(WUBOOK_DATE_PATTERN), end.toString(WUBOOK_DATE_PATTERN));
                Map<String, Object> dayOne = (Map<String, Object>) originalRoomsValues.get(rid.toString())[0];
                Assert.assertNotNull(dayOne, "dayOne");
                Map<String, Object> dayTwo = (Map<String, Object>) originalRoomsValues.get(rid.toString())[1];
                Assert.assertNotNull(dayTwo, "dayTwo");

                final Double priceOne = (Double) dayOne.get("price");
                Assert.assertNotNull(priceOne, "priceOne");
                final Double priceTwo = (Double) dayTwo.get("price");
                Assert.assertNotNull(priceTwo, "priceTwo");

                //Increase prices
                Object[] rooms = {
                        //1st room
                        new HashMap<String, Object>() {{
                            put("id", rid);
                            put("days", new Object[]{
                                    // Day 1
                                    new HashMap<String, Object>() {{
                                        put("price", priceOne + 2);
                                        put("min_stay", 10);
                                    }},
                                    // Day 2
                                    new HashMap<String, Object>() {{
                                        put("price", priceTwo + 3);
                                        put("no_ota", 8);
                                        put("min_stay_arrival", 15);
                                        put("closed_departure", 14);
                                        put("min_stay", 13);
                                        put("closed_arrival", 12);
                                        put("closed", 11);
                                        put("avail", 10);
                                        put("max_stay", 9);
                                    }}
                            });
                        }}
                };
                if (logger.isTraceEnabled()) {
                    new Printer().print(rooms);
                }

                //Ready, Set, GO!!!11
                String result = wubook.updateRoomsValues(token, start.toString(WUBOOK_DATE_PATTERN), rooms);

                //Verify our update worked out
                Assert.assertEquals(result, "Ok");
                if (logger.isTraceEnabled()) {
                    logger.trace(result);
                }
                Map<String, Object[]> resultFetch = wubook.fetchRoomsValues(token, start.toString(WUBOOK_DATE_PATTERN), end.toString(WUBOOK_DATE_PATTERN));
                if (logger.isTraceEnabled()) {
                    logger.trace("Price Values for: " + start.toString(WUBOOK_DATE_PATTERN) + " - " + end.toString(WUBOOK_DATE_PATTERN));
                    new Printer().print(resultFetch);
                }
                Map<String, Object> resultDayOne = (Map<String, Object>) resultFetch.get(rid.toString())[0];
                Assert.assertEquals(resultDayOne.get("price"), priceOne + 2);
                Map<String, Object> resultDayTwo = (Map<String, Object>) resultFetch.get(rid.toString())[1];
                Assert.assertEquals(resultDayTwo.get("price"), priceTwo + 3);
                return null;
            }
        }.call();
    }

    @Test(enabled = false)
    public void testUpdateSparseRoomsValues() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                final LocalDate first = new LocalDate().plusWeeks(1);
                final LocalDate second = first.plusDays(2);
                final LocalDate third = second.plusDays(2);

                final Long rid = getArbitraryRoom(wubook, token);

                //Remember original values
                Map<String, Object[]> originalRoomsValues = wubook.fetchRoomsValues(token, first.toString(WUBOOK_DATE_PATTERN), third.toString(WUBOOK_DATE_PATTERN));
                Map<String, Object> dayOne = (Map<String, Object>) originalRoomsValues.get(rid.toString())[0];
                Assert.assertNotNull(dayOne, "dayOne");
                Map<String, Object> dayTwo = (Map<String, Object>) originalRoomsValues.get(rid.toString())[2];
                Assert.assertNotNull(dayTwo, "dayTwo");
                Map<String, Object> dayThree = (Map<String, Object>) originalRoomsValues.get(rid.toString())[4];
                Assert.assertNotNull(dayThree, "dayThree");

                final Double priceOne = (Double) dayOne.get("price");
                Assert.assertNotNull(priceOne, "priceOne");
                final Double priceTwo = (Double) dayTwo.get("price");
                Assert.assertNotNull(priceTwo, "priceTwo");
                final Double priceThree = (Double) dayThree.get("price");
                Assert.assertNotNull(priceThree, "priceThree");

                //Increase prices
                Object[] rooms = {
                        //1st room
                        new HashMap<String, Object>() {{
                            put("id", rid);
                            put("days", new Object[]{
                                    // Day 1
                                    new HashMap<String, Object>() {{
                                        put("price", priceOne + 2);
                                        put("date", first.toString(WUBOOK_DATE_PATTERN));
                                    }},
                                    // Day 2
                                    new HashMap<String, Object>() {{
                                        put("price", priceTwo + 3);
                                        put("date", second.toString(WUBOOK_DATE_PATTERN));
                                    }},
                                    // Day 2
                                    new HashMap<String, Object>() {{
                                        put("price", priceTwo + 3);
                                        put("date", third.toString(WUBOOK_DATE_PATTERN));
                                    }}
                            });
                        }}
                };
                if (logger.isTraceEnabled()) {
                    new Printer().print(rooms);
                }

                //Ready, Set, GO!!!11
                String result = wubook.updateSparseRoomValues(token, rooms);

                //Verify our update worked out
                Assert.assertEquals(result, "Ok");
                if (logger.isTraceEnabled()) {
                    logger.trace(result);
                }
                Map<String, Object[]> resultFetch = wubook.fetchRoomsValues(token, first.toString(WUBOOK_DATE_PATTERN), third.toString(WUBOOK_DATE_PATTERN));
                if (logger.isTraceEnabled()) {
                    logger.trace("Price Values for: " + first.toString(WUBOOK_DATE_PATTERN) + " - " + third.toString(WUBOOK_DATE_PATTERN));
                    new Printer().print(resultFetch);
                }
                Map<String, Object> resultDayOne = (Map<String, Object>) resultFetch.get(rid.toString())[0];
                Assert.assertEquals(resultDayOne.get("price"), priceOne + 2);
                Map<String, Object> resultDayTwo = (Map<String, Object>) resultFetch.get(rid.toString())[2];
                Assert.assertEquals(resultDayTwo.get("price"), priceTwo + 3);
                Map<String, Object> resultDayThree = (Map<String, Object>) resultFetch.get(rid.toString())[4];
                Assert.assertEquals(resultDayThree.get("price"), priceThree + 3);
                return null;
            }
        }.call();
    }

    private Long getArbitraryRoom(Wubook wubook, String token) {
        List<Map<String, Object>> roomTypes = wubook.fetchRooms(token);
        Assert.assertFalse(roomTypes.isEmpty());
        Long rid = ((Number) roomTypes.get(0).get("id")).longValue();
        Assert.assertNotNull(rid, "rid");
        return rid;
    }

    @Test
    public void testGetPlans() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                List<Map<String, Object>> plans = wubook.getPlans(token);
                new Printer().print(plans);
                return null;
            }
        }.call();
    }

    @Test
    public void testFetchBookings() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Object[] result = wubook.fetchNewBookings(token);
//                Object[] result = wubook.fetchBookings(token, "29/01/2014", "29/01/2014");
                //Each array cell contains a separate booking (itself being a Map)
                new Printer().print(result);
                return null;
            }
        }.call();
    }

    @Test(enabled = false)
    public void testPushActivation() {
        final String url = "http://144.76.2.181:8080/wubooking/catch";
//        final String url = "http://88.198.41.177:8081/wubooking/catch";
//        final String url = "http://vomelio.no-ip.biz:8888/wubooking/catch";
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Object result = wubook.pushActivation(token, url);
                Assert.assertEquals(result, "Ok");
                new Printer().print(result);
                return null;
            }
        }.call();
    }

    @Test
    public void testGetPushUrl() {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Object result = wubook.getPushUrl(token);
                System.out.println("result = " + result);
                new Printer().print(result);
                return null;
            }
        }.call();
    }

    private static void assertRoomFound(int expectedSize, String shortname, long rid, List<Map<String, Object>> roomTypes, String name, Integer capacity, Integer children) {
        logger.debug("Fetched " + roomTypes.size() + " room types");
        boolean found = false;
        for (Map<String, Object> roomType : roomTypes) {
            if ((int) roomType.get("id") == (int) rid) {
                found = true;
                logger.debug("roomType = " + roomType);
                Assert.assertEquals(roomType.get("name"), name);
                Assert.assertEquals(roomType.get("shortname"), shortname);
//                Assert.assertEquals(roomType.getCapacity(), capacity);
                Assert.assertEquals(roomType.get("children"), children);
//                Assert.assertEquals(roomType.getDefaultPrice(),10.0);//TODO find out how to check it
//                Assert.assertEquals(roomType.getOtaRooms(), 4);//'avail' is obtained from 'fetch_rooms_values' request
            }
        }
        Assert.assertTrue(found, "Could not fetch expected roomType");
        Assert.assertEquals(roomTypes.size(), expectedSize);
    }

    @Test(enabled = false)
    public void testWErrors() throws MalformedURLException, XmlRpcException {
        new WubookCaller<Void>(ACCOUNT) {
            @Override
            protected Void run(Wubook wubook, String token) {
                try {
                    WubookImpl.printErrors(token);
                } catch (Exception e) {
                    Assert.fail("Failed", e);
                }
                return null;
            }
        }.call();
    }
}
