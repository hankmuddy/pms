package com.idgindigo.pms.web.websocket;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.security.CustomUserDetails;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.VerificationsInOrder;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author vomel
 * @since 30.12.13 12:57
 */
public class WebSocketEndpointTest {
    private static final String CONNECTIONS = "connections";
    private static final String TENANT = "greatest_tenant_ever";
    private static final String USERNAME = "Fomych";
    private static final String PASSWORD = "user_password";
    private static final String EVIL_TWIN = " - evil twin";
    private static long id;

    @BeforeMethod
    public void beforeMethod() {
        Deencapsulation.setField(WebSocketEndpoint.class, CONNECTIONS, new CopyOnWriteArraySet<>());
    }

    @Test
    public void testLogin() throws IOException {
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(new CustomUserDetails(new HotelUser() {{
            setId(id++);
            setHotel(new Hotel() {{
                setId(id++);
                setTenantId(TENANT);
            }});
//            setUserType(USER);
            setUsername(USERNAME);
        }}), PASSWORD);

        Set<WebSocketEndpoint> connections = Deencapsulation.getField(WebSocketEndpoint.class, CONNECTIONS);
        Assert.assertEquals(connections.size(), 0);
        new NonStrictExpectations() {{
            sessionFomych.getUserPrincipal();
            result = auth;
            sessionFomych.getBasicRemote();
            result = basicFomych;
        }};
        new WebSocketEndpoint().newSession(sessionFomych);
        new VerificationsInOrder() {{
//            basicFomych.sendText("User Fomych of hotel greatest_tenant_ever has connected.");
        }};
        Assert.assertEquals(connections.size(), 1);
    }

    @Mocked
    private Session sessionFomych;
    @Mocked
    private Session sessionFomychNokla;
    @Mocked
    private Session sessionSpouse;
    @Mocked
    private Session sessionEvilTwin;
    @Mocked
    private RemoteEndpoint.Basic basicFomych;
    @Mocked
    private RemoteEndpoint.Basic basicFomychNokla;
    @Mocked
    private RemoteEndpoint.Basic basicSpouse;
    @Mocked
    private RemoteEndpoint.Basic basicEvilTwin;
    @Mocked
    private Logger logger;

    @Test
    public void testBroadcast() throws IOException {
        Logger loggerOriginal = Deencapsulation.getField(WebSocketEndpoint.class, "logger");
        Deencapsulation.setField(WebSocketEndpoint.class, "logger", logger);
        new NonStrictExpectations() {{
            sessionFomych.getBasicRemote();
            result = basicFomych;
            sessionFomych.isOpen();
            result = true;
            sessionFomychNokla.getBasicRemote();
            result = basicFomychNokla;
            sessionFomychNokla.isOpen();
            result = true;
            sessionEvilTwin.getBasicRemote();
            result = basicEvilTwin;
            sessionEvilTwin.isOpen();
            result = true;
            sessionSpouse.getBasicRemote();
            result = basicSpouse;
            sessionSpouse.isOpen();
            result = true;
        }};
        Set<WebSocketEndpoint> connections = Deencapsulation.getField(WebSocketEndpoint.class, CONNECTIONS);
        Assert.assertEquals(connections.size(), 0);
        connections.add(createEndpoint(USERNAME, TENANT, sessionFomych));
        connections.add(createEndpoint(USERNAME, TENANT, sessionFomychNokla));
        connections.add(createEndpoint(USERNAME + EVIL_TWIN, TENANT + EVIL_TWIN, sessionEvilTwin));
        connections.add(createEndpoint(USERNAME + "'s spouse", TENANT, sessionSpouse));

        WebSocketEndpoint.broadcast("Test message", TENANT);
        WebSocketEndpoint.broadcast("Another test message", TENANT + EVIL_TWIN);
        WebSocketEndpoint.broadcast("Yet another test message", "Wrong Tenant");

        new VerificationsInOrder() {{
            basicFomych.sendText("Test message");
            basicFomychNokla.sendText("Test message");
            basicEvilTwin.sendText("Test message");
            times = 0;
            basicSpouse.sendText("Test message");

            basicEvilTwin.sendText("Another test message");
            basicFomych.sendText(anyString);
            times = 0;
            basicFomychNokla.sendText(anyString);
            times = 0;
            basicSpouse.sendText(anyString);
            times = 0;

            basicEvilTwin.sendText(anyString);
            times = 0;
            basicFomych.sendText(anyString);
            times = 0;
            basicFomychNokla.sendText(anyString);
            times = 0;
            basicSpouse.sendText(anyString);
            times = 0;
            logger.warn("Could not find connected users of tenant Wrong Tenant to broadcast to");
        }};
        Deencapsulation.setField(WebSocketEndpoint.class, "logger", loggerOriginal);
        Assert.assertEquals(connections.size(), 4);
    }

    @Test
    public void testPush() throws IOException {
        Logger loggerOriginal = Deencapsulation.getField(WebSocketEndpoint.class, "logger");
        Deencapsulation.setField(WebSocketEndpoint.class, "logger", logger);
        new NonStrictExpectations() {{
            sessionFomych.getBasicRemote();
            result = basicFomych;
            sessionFomych.isOpen();
            result = true;
            sessionFomychNokla.getBasicRemote();
            result = basicFomychNokla;
            sessionFomychNokla.isOpen();
            result = true;
            sessionEvilTwin.getBasicRemote();
            result = basicEvilTwin;
            sessionEvilTwin.isOpen();
            result = true;
            sessionSpouse.getBasicRemote();
            result = basicSpouse;
            sessionSpouse.isOpen();
            result = true;
        }};
        Set<WebSocketEndpoint> connections = Deencapsulation.getField(WebSocketEndpoint.class, CONNECTIONS);
        Assert.assertEquals(connections.size(), 0);

        connections.add(createEndpoint(USERNAME, TENANT, sessionFomych));
        connections.add(createEndpoint(USERNAME, TENANT, sessionFomychNokla));
        connections.add(createEndpoint(USERNAME + EVIL_TWIN, TENANT + EVIL_TWIN, sessionEvilTwin));
        connections.add(createEndpoint(USERNAME + "'s spouse", TENANT, sessionSpouse));

        WebSocketEndpoint.push("The Message", USERNAME, TENANT);
        WebSocketEndpoint.push("The Wrong Message", USERNAME, "Wrong Tenant");

        new VerificationsInOrder() {{
            basicFomych.sendText("The Message");
            basicFomychNokla.sendText("The Message");
            basicEvilTwin.sendText(anyString);
            times = 0;
            basicSpouse.sendText(anyString);
            times = 0;
            logger.warn("Could not find connected user Fomych of tenant Wrong Tenant to send message to");
        }};
        Deencapsulation.setField(WebSocketEndpoint.class, "logger", loggerOriginal);
        Assert.assertEquals(connections.size(), 4);
    }

    @Test
    public void testClose() {
        Set<WebSocketEndpoint> connections = Deencapsulation.getField(WebSocketEndpoint.class, CONNECTIONS);
        Assert.assertEquals(connections.size(), 0);

        WebSocketEndpoint wsFomychNokla;
        connections.add(wsFomychNokla = createEndpoint(USERNAME, TENANT, sessionFomychNokla));
        String username = USERNAME;
        String tenant = TENANT;
        Session sessionFomych1 = sessionFomych;
        connections.add(createEndpoint(username, tenant, sessionFomych1));
        connections.add(createEndpoint(USERNAME + EVIL_TWIN, TENANT + EVIL_TWIN, sessionEvilTwin));
        connections.add(createEndpoint(USERNAME + "'s spouse", TENANT, sessionSpouse));

        Assert.assertEquals(connections.size(), 4);

        wsFomychNokla.handleClose();

        Assert.assertEquals(connections.size(), 3);

        Assert.assertFalse(connections.contains(wsFomychNokla));
    }

    private static WebSocketEndpoint createEndpoint(String username, String tenantId, Session session) {
        WebSocketEndpoint endpoint = new WebSocketEndpoint();
        Deencapsulation.setField(endpoint, "username", username);
        Deencapsulation.setField(endpoint, "tenantId", tenantId);
        Deencapsulation.setField(endpoint, "session", session);
        return endpoint;
    }

    @Test(dataProvider = "isHotelUser")
    public void testIsHotelUser(Authentication user, boolean expected) {
        boolean result = WebSocketEndpoint.isHotelUser(user);
        Assert.assertEquals(result, expected);
    }

    @DataProvider(name = "isHotelUser")
    public Object[][] getData() {
        List<Object[]> result = new ArrayList<>(8);
        result.add(new Object[]{new Admin(), false});
        result.add(new Object[]{new HotelUser(), true});
        result.add(new Object[]{new Manager(), false});
        result.add(new Object[]{new ManagerSupervisor(), false});
        return result.toArray(new Object[result.size()][]);
    }

}
