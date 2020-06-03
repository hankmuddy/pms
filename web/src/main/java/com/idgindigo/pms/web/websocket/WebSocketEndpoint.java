package com.idgindigo.pms.web.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateDeserializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateSerializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateTimeDeserializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateTimeSerializer;
import com.idgindigo.pms.security.CustomUserDetails;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.idgindigo.pms.service.pms.RoomUseService.GroupRoomUseDto;
import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;

/**
 * @author vomel
 * @since 27.12.13 18:13
 */
@ServerEndpoint(value = "/websocket", configurator = SpringConfigurator.class)
public class WebSocketEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private static final Set<WebSocketEndpoint> connections = new CopyOnWriteArraySet<>();
    private static final String DISCONNECTED = "User %s of hotel %s has disconnected.";
    private static final String MESSAGE = "%s:User %s: %s";
    private static final String LOGGED_IN = "User %s has connected.";
    private static final String HOTEL_LOGGED_IN = "User %s of hotel %s has connected.";
    private static final String COULD_NOT_CONNECT = "Could not connect %s";
    private static final String TENANT_NOT_LOGGED_IN = "Could not find connected users of tenant %s to broadcast to";
    private static final String USER_NOT_LOGGED_IN = "Could not find connected user %s of tenant %s to send message to";
    private static final String FORCELY_DISCONNECTED = "User %s has been forcely disconnected.";
    private static final String TRACE_MESSAGE = "Sending '%s' to user '%s' of hotel '%s'";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Hibernate4Module())
            .registerModule(new JodaModule()
                    .addSerializer(LocalDate.class, new CustomLocalDateSerializer())
                    .addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer())
                    .addDeserializer(LocalDate.class, new CustomLocalDateDeserializer())
                    .addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer()));

    private String username;
    private String tenantId;
    private Session session;

    public static void push(String message, String username, String tenantId) {
        boolean notFound = true;
        for (WebSocketEndpoint connection : connections) {
            if (canPushTo(connection, tenantId, username)) {
                sendMessage(message, connection);
                notFound = false;
            }
        }
        if (notFound) {
            logger.warn(String.format(USER_NOT_LOGGED_IN, username, tenantId));
        }
    }

    public static void broadcast(Object data, String code, String tenantId) {
        try {
            //TODO remove this ugly hack!!!!
            if (data instanceof RoomUse) wipeSeasons((RoomUse) data);
            else if (data instanceof GroupRoomUseDto) for (RoomUseWithOverrides r : ((GroupRoomUseDto) data).getRoomUses()) wipeSeasons(r.getRoomUse());
            broadcast(mapper.writerWithView(BaseEntity.SoloView.class).writeValueAsString(new WebSocketMessage(code, data)), tenantId);
        } catch (Exception e) {
            logger.warn("{}:Was unable to broadcast {}", tenantId, data, e);
        }
    }

    private static void wipeSeasons(RoomUse data) {
        data.getPlan().setSeasons(null);
    }

    public static void broadcast(String message, String tenantId) {
        boolean notFound = true;
        for (WebSocketEndpoint connection : connections) {
            if (equal(tenantId, connection)) {
                sendMessage(message, connection);
                notFound = false;
            }
        }
        if (notFound) {
            logger.warn(String.format(TENANT_NOT_LOGGED_IN, tenantId));
        }
    }

    public static void broadcast(String message) {
        try {
            String jsonMessage = mapper.writerWithView(BaseEntity.SoloView.class).writeValueAsString(new WebSocketMessage(WebSocketMessage.SYSTEM_ALERT, message));
            for (WebSocketEndpoint connection : connections) {
                sendMessage(jsonMessage, connection);
            }
        } catch (JsonProcessingException e) {
            logger.warn("Was unable to broadcast {}", message, e);
        }
    }

    private static boolean canPushTo(WebSocketEndpoint connection, String tenantId, String username) {
        return tenantId.equals(connection.tenantId) && connection.username.equals(username);
    }

    /*private static boolean isAdmin(String username, String tenantId, WebSocketEndpoint connection) {
        return tenantId == null && connection.username.equals(username);
    }

    private static boolean isTenantUser(String username, String tenantId, WebSocketEndpoint connection) {
        return tenantId != null && connection.tenantId.equals(tenantId) && connection.username.equals(username);
    }*/

    @OnOpen
    public void newSession(Session session) {
        this.session = session;
        Assert.notNull(session.getUserPrincipal(), "UserPrincipal");
        username = session.getUserPrincipal().getName();
        com.idgindigo.pms.logins.domain.Authentication authentication = extractAuthentication(session);
        if (isTenantDefined(authentication)) {
            tenantId = authentication.getHotel().getTenantId();
        }
        connections.add(this);
        String message = String.format(isHotelUser(authentication) ? HOTEL_LOGGED_IN : LOGGED_IN, username, tenantId);
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
//        sendMessage(message, this);
    }

    private boolean isTenantDefined(com.idgindigo.pms.logins.domain.Authentication authentication) {
        return authentication.getHotel() != null;
    }

    static boolean isHotelUser(com.idgindigo.pms.logins.domain.Authentication authentication) {
        return authentication.getUserType().equals(HotelUser.USER);
    }

    private static com.idgindigo.pms.logins.domain.Authentication extractAuthentication(Session session) {
        return ((CustomUserDetails) ((Authentication) session.getUserPrincipal()).getPrincipal()).getAuthentication();
    }

    @OnMessage
    public void processTextMessage(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(MESSAGE, tenantId, username, HtmlFilter.filter(message)));
        }
    }

    @OnClose
    public void handleClose() {
        connections.remove(this);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(DISCONNECTED, username, tenantId));
        }
    }

    @OnError
    public void handleError(Throwable t) {
        logger.warn("{}:{}:handleError: {}", tenantId, username, t.getMessage());
    }

    private static void sendMessage(String message, WebSocketEndpoint endpoint) {
        try {
            if (endpoint.session.isOpen()) {
                endpoint.session.getBasicRemote().sendText(message);
                if (logger.isTraceEnabled()) {
                    logger.trace(String.format(TRACE_MESSAGE, message, endpoint.username, endpoint.tenantId));
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("{}:Detected a closed session, disconnecting...", endpoint.tenantId);
                }
                removeAndClose(endpoint);
            }

        } catch (Exception e) {
            processError(endpoint, e);
        }
    }

    private static void processError(WebSocketEndpoint endpoint, Exception e) {
        logger.warn(String.format(COULD_NOT_CONNECT, endpoint), e);
        removeAndClose(endpoint);
//        broadcast(String.format(FORCELY_DISCONNECTED, endpoint.username), endpoint.tenantId);
    }

    private static void removeAndClose(WebSocketEndpoint endpoint) {
        connections.remove(endpoint);
        try {
            endpoint.session.close();
        } catch (IOException ioe) {
            // Ignore
        } catch (Exception e) {
            logger.warn("Error while closing session for endpoint {}", endpoint, e);
        }
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("username", username)
                .append("tenantId", tenantId)
                .toString();
    }

    private void closeAll() {
        for (WebSocketEndpoint connection : connections) {
            try {
                connection.session.close();
            } catch (IOException e) {
                //ignored
            }
            connections.remove(connection);
        }
    }

    public static boolean isOnline(String tenantId, String username) {
        for (WebSocketEndpoint connection : connections) {
            if (connection.session.isOpen()) {
                if (connection.username.equals(username) && (bothNull(tenantId, connection) || equal(tenantId, connection))) {
                    return true;
                }
            } else {
                connections.remove(connection);
            }
        }
        return false;
    }

    public static boolean equal(String tenantId, WebSocketEndpoint connection) {
        return connection.tenantId != null && connection.tenantId.equals(tenantId);
    }

    public static boolean bothNull(String tenantId, WebSocketEndpoint connection) {
        return connection.tenantId == null && tenantId == null;
    }

    //For Testing purposes
    private static class MyThread extends Thread {
        private final WebSocketEndpoint webSocketEndpoint;

        MyThread(WebSocketEndpoint webSocketEndpoint) {
            this.webSocketEndpoint = webSocketEndpoint;
        }

        @Override
        public void run() {
            try {
                System.out.println("waiting");
                Thread.sleep(2000);
                System.out.println("closing");
                webSocketEndpoint.closeAll();
            } catch (InterruptedException e) {
                //ignored;
            }
        }
    }


}