package com.idgindigo.pms.security;

import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vomel
 * @since 27.04.14 21:29
 */
@Component
public class LockoutDetailsService implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LockoutDetailsService.class);
    private static final ConcurrentHashMap<UserData, LockoutData> lockMap = new ConcurrentHashMap<>();
    static final int DEFAULT_MAX_ATTEMPTS = 5;

    private static int defaultLockoutMillis;

    @Value("${defaultLockoutMillis}")
    public void setLockoutMillis(Integer m) {
        defaultLockoutMillis = m;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String principal = event.getAuthentication().getName();
        String[] username = CustomUserDetailsService.SEPARATE.split(principal);
        String origin = ((WebAuthenticationDetails) ((Authentication) event.getSource()).getDetails()).getRemoteAddress();
        String cause = String.valueOf(event.getException());
        if (username.length == 2) {
            logger.info("Failed authentication for user '{}' of tenant '{}' from ip {} caused by {}", username[0], username[1], origin, cause);
            if (event instanceof AuthenticationFailureBadCredentialsEvent) {
                registerFailedLogin(username[0], username[1], event.getTimestamp());
            }
        } else {
            logger.info("Failed authentication for user '{}' from ip {} caused by {}", username.length == 1 ? username[0] : principal, origin, cause);
            if (event instanceof AuthenticationFailureBadCredentialsEvent && username.length > 0) {
                registerFailedLogin(username[0], null, event.getTimestamp());
            }
        }
    }

    private static class LockoutData {
        private int attempts;
        private long lastMillis;

        @Override
        public String toString() {
            return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                    .append("attempts", attempts)
                    .append("lastMillis", lastMillis)
                    .toString();
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class UserData {
        private String username;
        private String tenantId;
    }

    private static void registerFailedLogin(String username, String tenantId, long timestamp) {
        if (!isLockedOut(username, tenantId)) {
            LockoutData data = getData(username, tenantId);
            data.attempts++;
            data.lastMillis = timestamp;
            logger.debug("Recorded unsuccessful login of {}/{} attempts:{}, last at:{}", username, tenantId, data.attempts, data.lastMillis);
        }
    }

    static void registerSuccessLogin(String username, String tenantId) {
        getData(username, tenantId).attempts = 0;
    }

    static boolean isLockedOut(String username, String tenantId) {
        LockoutData data = getData(username, tenantId);
        if (data.attempts >= DEFAULT_MAX_ATTEMPTS) {
            long last = System.currentTimeMillis() - data.lastMillis;
            if (last < defaultLockoutMillis) {
                logger.debug("user {} of tenant {} was locked after {} attempts from {} for {} milliseconds", username, tenantId, data.attempts, new Date(data.lastMillis), defaultLockoutMillis);
                return true;
            }
        }
        return false;
    }

    private static LockoutData getData(String username, String tenantId) {
        UserData userData = new UserData(username, tenantId);
        lockMap.putIfAbsent(userData, new LockoutData());
        return lockMap.get(userData);
    }

}
