package com.idgindigo.pms.channel.wubook;

import com.idgindigo.pms.axmlrpc.XmlRpcClient;
import com.idgindigo.pms.axmlrpc.XmlRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author vomel
 * @since 16.12.13 17:38
 */
public class WubookImpl implements Wubook {
    private static final Logger logger = LoggerFactory.getLogger(WubookImpl.class);

    private static final String WUBOOK_ACCOUNT = "wubook.system.properties";
    private static final String MSG = "Cannot load " + WUBOOK_ACCOUNT;
    private static final int WODOO = 0;
    private static final String WUBOOK_URL;
    public static final boolean ENABLED;

    static {
        try {
            InputStream is = WubookImpl.class.getClassLoader().getResourceAsStream(WUBOOK_ACCOUNT);
            Properties settings = new Properties();
            settings.load(is);
            if (logger.isDebugEnabled()) {
                logger.debug("settings = {}", settings);
            }
            WUBOOK_URL = settings.getProperty("wubook.url");
            ENABLED = Boolean.parseBoolean(settings.getProperty("wubook.enabled"));
        } catch (Exception e) {
            logger.error(MSG, e);
            throw new IllegalArgumentException(MSG, e);
        }
    }

    private final String username;
    private final String password;
    private final String lcode;

    public WubookImpl(WubookAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("WubookAccount is null");
        }
        username = account.getUsername();
        password = account.getPassword();
        lcode = account.getLcode();
    }

    @Override
    public long newRoom(String token, String name, int beds, double defprice, int avail, String shortname, String defboard) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("new_room", token, lcode, WODOO, name, beds, defprice, avail, shortname, defboard.toLowerCase());
    }

    @Override
    public long newVirtualRoom(String token, long rid, String name, int beds, int children, double defprice, String shortname, String defboard) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("new_virtual_room", token, lcode, rid, WODOO, name, beds, children, defprice, shortname, defboard.toLowerCase());
    }

    @Override
    public long modRoom(String token, long rid, String name, int beds, double defprice, int avail, String shortname, String defboard) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("mod_room", token, lcode, rid, name, beds, defprice, avail, shortname, defboard.toLowerCase());
    }

    @Override
    public long modVirtualRoom(String token, long rid, String name, int beds, int children, double defprice, String shortname, String defboard) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("mod_virtual_room", token, lcode, rid, name, beds, children, defprice, shortname, defboard.toLowerCase());
    }

    @Override
    public List<Map<String, Object>> fetchRooms(String token) {
        return new Caller<List<Map<String, Object>>>() {
            @Override
            List<Map<String, Object>> processSuccessfulResult(Object o) {
                Object[] rooms = (Object[]) o;
                List<Map<String, Object>> roomTypes = new ArrayList<>();
                for (Object roomObject : rooms) {
                    roomTypes.add((Map<String, Object>) roomObject);
                }
                return roomTypes;
            }
        }.callMethod("fetch_rooms", token, lcode);
    }

    @Override
    public void delRoom(String token, long rid) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("del_room", token, lcode, rid);
    }

    //((Map<String,Object>) ((Object[]) ((Map.Entry<String,Object[]>) ((Map<String,Object[]>) o).entrySet().iterator().next()).getValue())[0])
    @Override
    public Map<String, Object[]> fetchRoomsValues(String token, String dfrom, String dto) {
        return new Caller<Map<String, Object[]>>() {
            //TODO Convert to something useful
            @Override
            Map<String, Object[]> processSuccessfulResult(Object o) {
                return (Map<String, Object[]>) o;
            }
        }.callMethod("fetch_rooms_values", token, lcode, dfrom, dto);
    }

    @Override
    public String updateRoomsValues(String token, String dfrom, Object[] rooms) {
        return new Caller<String>() {
            @Override
            String processSuccessfulResult(Object o) {
                return (String) o;
            }
        }.callMethod("update_rooms_values", token, lcode, dfrom, rooms);
    }

    @Override
    public String updateSparseRoomValues(String token, Object[] rooms) {
        return new Caller<String>() {
            @Override
            String processSuccessfulResult(Object o) {
                return (String) o;
            }
        }.callMethod("update_sparse_rooms_values", token, lcode, rooms);
    }

    @Override
    public long addPlan(String token, String name) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("add_plan", token, lcode, name);
    }

    @Override
    public List<Map<String, Object>> getPlans(String token) {
        return new Caller<List<Map<String, Object>>>() {
            @Override
            List<Map<String, Object>> processSuccessfulResult(Object o) {
                Object[] rooms = (Object[]) o;
                List<Map<String, Object>> plans = new ArrayList<>(rooms.length);
                for (Object planObject : rooms) {
                    plans.add((Map<String, Object>) planObject);
                }
                return plans;
            }
        }.callMethod("get_plans", token, lcode);
    }

    @Override
    public Object[] getPlans(String token, long pid) {
        return new Caller<Object[]>() {
            //TODO Convert to something useful
            @Override
            Object[] processSuccessfulResult(Object o) {
                return (Object[]) o;
            }
        }.callMethod("get_plans", token, lcode, pid);
    }

    @Override
    public void delPlan(String token, long pid) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("del_plan", token, lcode, pid);
    }

    @Override
    public void updatePlanRack(String token, long pid, Map<String, Object[]> rack) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("update_plan_rack", token, Integer.parseInt(lcode), pid, rack);
    }

    @Override
    public void updatePlanPeriods(String token, long pid, Map<String, Object>[] periods) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("update_plan_periods", token, Integer.parseInt(lcode), pid, periods);
    }

    @Override
    public String getToken() {
        return new Caller<String>() {
            @Override
            String processSuccessfulResult(Object o) {
                return (String) o;
            }
        }.callMethod("get_token", username, password);
    }

    @Override
    public Map<String, Object[]> getChannelSymbols(String token) {
        return new Caller<Map<String, Object[]>>() {
            @Override
            Map<String, Object[]> processSuccessfulResult(Object o) {
                return (Map<String, Object[]>) o;
            }
        }.callMethod("get_channel_symbols", token);
    }

    @Override
    public void release(String token) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("release_token", token);
    }

    @Override
    public Object[] fetchNewBookings(String token) {
        return new Caller<Object[]>() {
            @Override
            Object[] processSuccessfulResult(Object o) {
                return (Object[]) o;
            }
        }.callMethod("fetch_new_bookings", token, lcode, Boolean.TRUE, 1);
    }

    @Override
    public Object[] fetchBookings(String token, String dfrom, String dto, Boolean oncreated) {
        return new Caller<Object[]>() {
            @Override
            Object[] processSuccessfulResult(Object o) {
                return (Object[]) o;
            }
        }.callMethod("fetch_bookings", token, lcode, dfrom, dto, oncreated, Boolean.TRUE);
    }

    @Override
    public Map<String, Object> fetchCc(String token, String rcode, String pwd) {
        return new Caller<Map<String, Object>>() {
            @Override
            Map<String, Object> processSuccessfulResult(Object o) {
                return (Map<String, Object>) o;
            }
        }.callMethod("fetch_cc", token, lcode, rcode, pwd);
    }

    @Override
    public String pushActivation(String token, String url) {
        return new Caller<String>() {
            @Override
            String processSuccessfulResult(Object o) {
                return String.valueOf(o);
            }
        }.callMethod("push_activation", token, lcode, url);
    }

    @Override
    public String getPushUrl(String token) {
        return new Caller<String>() {
            @Override
            String processSuccessfulResult(Object o) {
                return String.valueOf(o);
            }
        }.callMethod("push_url", token, lcode);
    }

    @Override
    public long addVPlan(String token, String name, long pid, int dtype, double value) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("add_vplan", token, lcode, name, pid, dtype, value);
    }

    @Override
    public long addRPlan(String token, String name, boolean compact) {
        return new Caller<Long>() {
            @Override
            Long processSuccessfulResult(Object o) {
                return ((Number) o).longValue();
            }
        }.callMethod("rplan_add_rplan", token, lcode, name, compact ? 1 : 0);
    }

    @Override
    public void updateRules(String token, long rpid, Map<String, Object> rules) {
        new Caller<Void>() {
            @Override
            Void processSuccessfulResult(Object o) {
                return null;
            }
        }.callMethod("rplan_update_rplan_rules", token, lcode, rpid, rules);
    }

    @Override
    public List<Map<String, Object>> getRPlans(String token) {
        return new Caller<List<Map<String, Object>>>() {
            @Override
            List<Map<String, Object>> processSuccessfulResult(Object o) {
                Object[] resolved = (Object[]) o;
                List<Map<String, Object>> plans = new ArrayList<>(resolved.length);
                for (Object planObject : resolved) {
                    plans.add((Map<String, Object>) planObject);
                }
                return plans;
            }
        }.callMethod("rplan_rplans", token, lcode);

    }

    static void printErrors(String token) throws MalformedURLException, XmlRpcException {
        XmlRpcClient client = new XmlRpcClient(new URL(WUBOOK_URL));
        Map<String, String> result2 = (Map<String, String>) client.callGeneric("werrors", token);
        List<Integer> list2 = new ArrayList<>(result2.keySet().size());
        for (String code : result2.keySet()) {
            list2.add(Integer.parseInt(code));
        }
        Collections.sort(list2);
        Collections.reverse(list2);
        for (Integer code2 : list2) {
            logger.debug("    int VAR_ = " + code2 + " ; //" + result2.get(String.valueOf(code2)));//NOSONAR
        }
    }

    private abstract static class Caller<T> {
        T callMethod(String method, Object... params) {
            Object[] result;
            try {
                XmlRpcClient client = new XmlRpcClient(WUBOOK_URL);
                Object res = client.callGeneric(method, params);
                result = res instanceof Object[] ? (Object[]) res : new Object[]{0, res};
                if (logger.isTraceEnabled()) {
                    String toString = Arrays.deepToString(result);
                    logger.trace("fetch_cc".equals(method) ? "{}: [secured]]" : "{}: {}", method, toString);
                }
                if (result.length == 0) throw new AssertionError("Protocol Error: empty result");
                if (result[0] == ((Integer) ResponseCode.OK)) {//NOSONAR
                    return processSuccessfulResult(result[1]);
                }
            } catch (Exception e) {
                logger.error("Failed to call wubook with params: {}, {}", method, Arrays.toString(params));
                throw new WubookException("RMLRPC parsing error", e);
            }
            throw new WubookException((Integer) result[0], String.valueOf(result[1]));
        }

        abstract T processSuccessfulResult(Object o);
    }

}
