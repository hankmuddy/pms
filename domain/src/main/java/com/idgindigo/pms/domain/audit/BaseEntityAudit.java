package com.idgindigo.pms.domain.audit;

import com.idgindigo.pms.domain.BaseEntity;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author valentyn_vakatsiienko
 * @since 6/3/14 3:23 PM
 */
@Component
public class BaseEntityAudit {

    private static EventListener AUDIT;

    @PrePersist
    public void injectCreatedBy(BaseEntity entity) {
        AUDIT.onCreate(entity);
    }

    @PreUpdate
    public void injectUpdatedBy(BaseEntity entity) {
        AUDIT.onUpdate(entity);
    }

    @Inject
    public void setAudit(EventListener audit) {
        BaseEntityAudit.AUDIT = audit;
    }
}
