package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock Controller to test incoming reservations from WuBook. To be disabled in production
 *
 * @author vomel
 * @since 30.01.14 15:21
 */
@Controller
@RequestMapping(MockWubook.URL)
public class MockWubook {
    public static final String CC = "cc";
    public static final String FETCH_BOOKINGS = "fetch_bookings";
    public static final String ADD_PLAN = "add_plan";
    public static final String FETCH_GROUP_BOOKINGS = "fetchGroupBookings";
    public static final String FETCH_NEW_BOOKINGS = "fetch_new_bookings";
    public static final String REFUSED_GROUP_BOOKINGS = "refusedGroupBookings";
    public static final String HEAVY_LOAD_1 = "heavyLoad1";
    public static final String HEAVY_LOAD_2 = "heavyLoad2";
    public static final String HEAVY_LOAD_3 = "heavyLoad3";
    public static final String HEAVY_LOAD_4 = "heavyLoad4";
    public static final String NEW_WAITING = "new_booking_status_waiting";
    public static final String NEW_APPROVED = "new_booking_status_approved";
    private static final Logger logger = LoggerFactory.getLogger(MockWubook.class);

    public static final String GET_TOKEN = "get_token";
    public static final String RELEASE_TOKEN = "release_token";
    public static final String UPDATE_ROOM_VALUES = "update_rooms_values";
    private static final List<String> responsesList = Arrays.asList(FETCH_NEW_BOOKINGS, ADD_PLAN, GET_TOKEN, RELEASE_TOKEN, UPDATE_ROOM_VALUES, FETCH_GROUP_BOOKINGS, REFUSED_GROUP_BOOKINGS, FETCH_BOOKINGS, HEAVY_LOAD_1, HEAVY_LOAD_2, HEAVY_LOAD_3, HEAVY_LOAD_4, CC, NEW_WAITING, NEW_APPROVED);
    private static final Map<String, String> mockResponses = new HashMap<>();

    static {
        for (String itemName : responsesList) {
            mockResponses.put(itemName, read(itemName));
        }
    }

    private static String read(String filename) {
        try {
            return StreamUtils.convertStreamToString(MockWubook.class.getClassLoader().getResourceAsStream("wubookresponses/" + filename + ".xml"));
        } catch (Exception e) {
            logger.error("Error parsing mock wubook xml responses from {}", filename, e);
            throw new IllegalArgumentException(e);
        }
    }

    public static final String URL = "mockwubook";

    @RequestMapping(value = "xrws", method = RequestMethod.POST)
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestString = StreamUtils.convertStreamToString(request.getInputStream());
        if (requestString == null) {
            return;
        }
        PrintWriter writer = response.getWriter();
        respond(requestString, writer);
        writer.flush();
        writer.close();
    }

    private static void respond(String requestString, PrintWriter writer) {
        for (String itemName : responsesList) {
            if (requestString.contains(itemName)) {
                writer.append(mockResponses.get(itemName));
                return;
            }
        }
    }

}
