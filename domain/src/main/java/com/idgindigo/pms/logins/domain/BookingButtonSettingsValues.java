package com.idgindigo.pms.logins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author vomel
 * @since 05.06.14 13:06
 */
@Entity
@Getter
@Setter
public class BookingButtonSettingsValues extends BaseIdentifiable {
    @ManyToOne(optional = false)
    @JsonIgnore
    private BookingButtonSettings bbs;
    @NotNull
    private String key;
    @NotNull
    private String value;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append("bbs_id", bbs != null ? bbs.getId() : null)
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
