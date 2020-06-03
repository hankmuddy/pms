package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.ListMembership;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/16/13 12:00 PM
 */
@Entity
@Getter
@Setter
public class Company extends BaseEntity {
    public static final String COMPANY = "company";

    @NotNull
    private String name;
    private String ownershipType;
    private String country;//TODO: validate iso code
    private String city;
    private String email;
    private String phone;
    private Long inn;
    private String registrationAddress;

    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private ListMembership listMembership;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private String membershipReason;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "CompanyDocuments")
    @Column(name = "document")
    private List<String> documents = new ArrayList<>();

    private String bankName;
    private String bankMfo;
    private String accountNumber;

    private String legalAddress;
    private String postAddress;

    @Max(100)
    @Min(0)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private int discount;
}
