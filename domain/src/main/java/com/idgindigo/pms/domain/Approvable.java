package com.idgindigo.pms.domain;

/**
 * @author vomel
 * @since 18.03.14 12:15
 */
public interface Approvable {
    Boolean getApproved();

    void setApproved(Boolean approved);
}
