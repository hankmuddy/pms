package com.idgindigo.pms.restutils.view;

import com.idgindigo.pms.domain.BaseEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author valentyn_vakatsiienko
 * @since 11/4/13 4:15 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseView {
    public Class<? extends BaseEntity.BaseView> value();
}
