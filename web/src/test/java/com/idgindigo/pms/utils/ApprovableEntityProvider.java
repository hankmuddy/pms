package com.idgindigo.pms.utils;

import com.idgindigo.pms.domain.ApprovableEntity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 11:59 AM
 */
public abstract class ApprovableEntityProvider<T extends ApprovableEntity> extends EntityProvider<T> {
    @Override
    public T getTransientEntity(final Visitor<T>... visitor) {
        return super.getTransientEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setApproved(true);
                for (Visitor<T> tVisitor : visitor) {
                    tVisitor.visit(entity);
                }
            }
        });
    }
}
