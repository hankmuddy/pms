package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vomel
 * @since 27.05.14 14:41
 */
@Entity
@Getter
@Setter
public class BookingButtonSettings extends BaseIdentifiable {
    @ManyToOne(optional = false)
    private Hotel hotel;
    private String name;
    private String language;
    private String textColor;
    private String currency;
    private String backgroundColor;
    @Column(nullable = false)
    private boolean cancel = true;
    private Integer width;
    private Integer height;
    @OneToMany(mappedBy = "bbs", fetch = FetchType.EAGER)
    private List<BookingButtonSettingsValues> keyValues = new ArrayList<>();

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("language", language)
                .append("textColor", textColor)
                .append("currency", currency)
                .append("backgroundColor", backgroundColor)
                .append("cancel", cancel)
                .append("width", width)
                .append("height", height)
                .toString();
    }
}
