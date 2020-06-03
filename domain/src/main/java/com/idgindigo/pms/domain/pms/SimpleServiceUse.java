package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:45 PM
 */
@Entity
@Getter
@Setter
public class SimpleServiceUse extends BaseServiceUse<SimpleService> {
    public static final String SIMPLE_SERVICE_USE = "simpleServiceUse";

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
        return SIMPLE_SERVICE_USE;
    }
}
