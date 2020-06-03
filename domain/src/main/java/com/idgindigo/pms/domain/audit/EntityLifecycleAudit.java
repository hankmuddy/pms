package com.idgindigo.pms.domain.audit;

import com.idgindigo.pms.domain.BaseIdentifiable;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author vomel
 * @since 13.12.13 12:59
 */
public class EntityLifecycleAudit {
    @PrePersist
    public void injectCreatedBy(BaseIdentifiable entity) {
        LocalDateTime utc = LocalDateTime.now(DateTimeZone.UTC).withMillisOfSecond(0);
        entity.setCreatedDate(utc);
        entity.setUpdatedDate(utc);

    }

    @PreUpdate
    public void injectUpdatedBy(BaseIdentifiable entity) {
        entity.setUpdatedDate(LocalDateTime.now(DateTimeZone.UTC).withMillisOfSecond(0));
    }
}