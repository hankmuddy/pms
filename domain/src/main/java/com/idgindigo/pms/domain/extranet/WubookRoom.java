package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.Approvable;


/**
 * @author vomel
 * @since 17.03.14 17:23
 */
public interface WubookRoom extends Approvable {

    String getName();

    void setName(String name);

    String getShortname();

    void setShortname(String shortname);

    Long getRid();

    void setRid(Long rid);

    Integer getAdults();

    void setAdults(Integer adults);

    Integer getChildren();

    void setChildren(Integer children);

    void setDefaultPrice(Long defaultPrice);

    Long getDefaultPrice();
}
