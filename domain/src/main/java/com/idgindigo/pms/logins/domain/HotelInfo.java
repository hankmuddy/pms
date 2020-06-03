package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.constraint.Country;
import com.idgindigo.pms.constraint.Currency;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/13/14 5:10 PM
 */
@Entity
@Getter
@Setter
@Check(constraints = "checkIn > earlyCheckInEnd and earlyCheckInEnd > earlyCheckInStart and checkOut < lateCheckOutStart and lateCheckOutStart < lateCheckOutEnd")
public class HotelInfo extends BaseIdentifiable {
    public static final int MINUTES_IN_DAY = 1440;

    @NotNull
    private String name;
    @NotNull
    @Country
    private String country;
    @NotNull
    private String city;
    private String logo;
    private String mainPhoto;
    private String postAddress;
    private String index;
    @NotNull
    private String officialAddress;
    private String bookPhone;
    private String accountPhone;
    private String infoPhone;
    private String email;
    private String webSite;
    @Column(length = 2000)
    private String description;

    @Min(0)
    private Integer multiplier;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    @NotNull
    private Integer checkIn;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    private Integer earlyCheckInStart;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    private Integer earlyCheckInEnd;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    @NotNull
    private Integer checkOut;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    private Integer lateCheckOutStart;
    @Min(0)
    @Max(MINUTES_IN_DAY)
    private Integer lateCheckOutEnd;
    private boolean earlyCheckInNoBreakfast;

    private float tourismTax;
    private boolean tourismTaxFromFullPrice;
    private float vat;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(updatable = false)
    private String paymentInfo;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(updatable = false)
    private String importantInfo;

    @NotNull
    private String timeZone;
    private Double longitude;
    private Double latitude;

    @Currency
    @NotNull
    private String currency;

    private String color;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class HotelInfoDto {
        private Boolean channelsEnabled;
        private HotelInfo hotelInfo;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("name", name)
                .appendSuper(super.toString())
                .toString();
    }
}
