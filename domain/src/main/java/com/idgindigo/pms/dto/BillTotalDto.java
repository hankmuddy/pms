package com.idgindigo.pms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author valentyn_vakatsiienko
 * @since 7/10/14 11:47 AM
 */
@Getter
@Setter
@NoArgsConstructor
public class BillTotalDto {
    private Long total;
    private Long totalPaid;

    public BillTotalDto(Long total, Long totalPaid) {
        this.total = total != null ? total : 0;
        this.totalPaid = totalPaid != null ? totalPaid : 0;
    }
}
