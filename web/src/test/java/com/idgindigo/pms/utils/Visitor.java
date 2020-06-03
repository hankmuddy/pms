package com.idgindigo.pms.utils;

/**
 * @author vomel
 * @since 15.11.13 13:50
 */
public interface Visitor<T> {
    void visit(T entity);
}
