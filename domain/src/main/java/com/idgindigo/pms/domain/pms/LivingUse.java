package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.extranet.service.Living;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 1/8/14 11:49 AM
 */
@Entity
@Getter
@Setter
public class LivingUse extends BaseServiceUse<Living> {
    public static final String LIVING_USE = "livingUse";

    private long livingAmount;
    private float tourismTax;

    @Column(nullable = false)
    @JsonIgnore
    private Boolean active = Boolean.TRUE;


    @Override
    public void setActive(Boolean active) {
        super.setActive(active);
        this.active = active;
    }

    @Override
    protected String getType() {
        return LIVING_USE;
    }
}
