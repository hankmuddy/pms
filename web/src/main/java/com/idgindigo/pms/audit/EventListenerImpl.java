package com.idgindigo.pms.audit;

import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.audit.EventListener;
import com.idgindigo.pms.security.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * @author valentyn_vakatsiienko
 * @since 6/3/14 3:26 PM
 */
@Component
public class EventListenerImpl implements EventListener {
    @Override
    public void onCreate(BaseEntity entity) {
        entity.setCreatedBy(SecurityUtils.getCurrentUser());
    }

    @Override
    public void onUpdate(BaseEntity entity) {
        entity.setUpdatedBy(SecurityUtils.getCurrentUser());
    }
}
