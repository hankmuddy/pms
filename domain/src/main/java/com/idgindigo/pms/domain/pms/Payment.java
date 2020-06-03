package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntity;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:29 AM
 */
@Entity
@Getter
@Setter
public class Payment extends ApprovableEntity {
    public static final String PAYMENT = "payment";

    @NotNull
    private LocalDateTime date;
    @NotNull
    private Long total;
    @ManyToOne
    @NotNull
    private Bill bill;
    //    @JsonView(SoloView.class)
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private BankDetails bankDetails;

}
