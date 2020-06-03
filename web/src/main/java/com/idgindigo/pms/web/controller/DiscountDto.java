package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.pms.BankDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 12:38 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    @Max(100)
    @Min(0)
    private int discount;
    private BankDetails bankDetails;

    public DiscountDto(int discount) {
        this(discount, null);
    }
}
