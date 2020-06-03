package com.idgindigo.pms.domain.extranet.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.constraint.Country;
import com.idgindigo.pms.constraint.Language;
import com.idgindigo.pms.domain.extranet.ListMembership;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 2:45 PM
 */
@Entity
@Getter
@Setter
@DiscriminatorValue(Adult.ADULT)
public class Adult extends Person {
    public static final String ADULT = "adult";

    @Country
    private String country;
    private String city;
    private String address;
    private String phone;
    @Column(unique = true)
    private String email;
    @Language
    private String language;

    private String passportNumber;
    private String passportValidTill;
    private String passportIssued;
    private Integer postIndex;

    private String cio;
    @Enumerated(EnumType.STRING)
    private EntryType entryType;
    private LocalDate entryValidFrom;
    private LocalDate entryValidTill;
    private String entryNumber;
    @Enumerated(EnumType.STRING)
    private VisaType visaType;

    private String carBrand;
    private String carNumber;

    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private ListMembership listMembership;
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private String membershipReason;

    @Max(100)
    @Min(0)
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private int discount;

    @Override
    public String getType() {
        return ADULT;
    }

    public enum EntryType {
        VISA, RESIDENCE_PERMIT, TEMP_RESIDENCE_PERMIT, NO_DOCUMENT
    }

    public enum VisaType {
        UNDEFINED, ENTRY, ENTRY_EXIT, COMMON, COMMON_PRIVATE, COMMON_BUSINESS, COMMON_TOURIST, COMMON_TOURIST_GROUP,
        COMMON_STUDY, COMMON_WORKING, COMMON_HUMANITARIAN, POLITICAL_ASYLUM, DUTY, DIPLOMATIC, TRANSIT_1, TRANSIT_2, TEMPORARY_RESIDENCE
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append("phone", phone)
                .append("email", email)
                .toString();
    }
}
