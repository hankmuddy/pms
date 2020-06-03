package com.idgindigo.pms.restutils.view;

import com.idgindigo.pms.domain.BaseEntity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/4/13 4:08 PM
 */
public interface DataView {
    boolean hasView();

    Class<? extends BaseEntity.BaseView> getView();

    Object getData();
}
