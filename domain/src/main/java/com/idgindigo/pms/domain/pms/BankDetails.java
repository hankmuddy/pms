package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 12:36 PM
 */
@Entity
@Getter
@Setter
public class BankDetails extends BaseEntity {
    public static final String BANK_DETAILS = "bankDetails";

    @NotNull
    private String name;

    private String bankName;
    @Pattern(regexp = "^[0-9]*$")
    private String account;
    private String swift;
    @Column(length = 1000)
    private String bankAddress;

    private String corrBankName;
    @Pattern(regexp = "^[0-9]*$")
    private String corrAccount;
    private String corrSwift;
    @Column(length = 1000)
    private String corrBankAddress;
    private String iban;

    private Long edrpou;
    private Long mfo;
    private Long cardNumber;
    private String holder;

    private String description;
    @Column(length = 1000)
    private String additional;
    @NotNull
    private PaymentType paymentType;
    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Boolean defaultDetails = Boolean.FALSE;
    @Column(nullable = false)
    private Boolean blocked = Boolean.FALSE;

    public enum PaymentType {
        CARD, CASH, TRANSFER, INTERNATIONAL_ACCOUNT
    }

}
