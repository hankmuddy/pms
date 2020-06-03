package com.idgindigo.pms.domain.audit;

import com.idgindigo.pms.domain.BaseEntity;

/**
 * @author valentyn_vakatsiienko
 * @since 6/3/14 3:09 PM
 */
public interface EventListener {
    void onCreate(BaseEntity entity);

    void onUpdate(BaseEntity entity);
}
