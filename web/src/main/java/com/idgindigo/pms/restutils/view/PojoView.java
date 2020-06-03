package com.idgindigo.pms.restutils.view;

import com.idgindigo.pms.domain.BaseEntity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/4/13 4:11 PM
 */
public class PojoView implements DataView {
    private final Object pojo;
    private final Class<? extends BaseEntity.BaseView> view;

    public PojoView(Object pojo, Class<? extends BaseEntity.BaseView> view) {
        this.pojo = pojo;
        this.view = view;
    }

    @Override
    public boolean hasView() {
        return true;
    }

    @Override
    public Class<? extends BaseEntity.BaseView> getView() {
        return view;
    }

    @Override
    public Object getData() {
        return pojo;
    }
}
