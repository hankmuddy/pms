package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/9/13 3:52 PM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Check(constraints = "(roomUse_id IS NULL OR group_id IS NULL) AND (roomUse_id IS NOT NULL OR group_id IS NOT NULL) AND (total = 0 OR bankDetails_id IS NOT NULL)")
public class Refund extends BaseEntity {
    public static final String REFUND = "refund";

    @OneToMany(mappedBy = REFUND)
    private List<BaseServiceUse> serviceUses = new ArrayList<>();
    @ManyToOne
    @JoinColumn(updatable = false)
    private RoomUse roomUse;
    @ManyToOne
    @JoinColumn(updatable = false)
    private CustomerGroup group;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    @Column(updatable = false, nullable = false)
    private Long total = 0L;
    @ManyToOne
    @JoinColumn(updatable = false)
    private BankDetails bankDetails;

    public boolean isForCustomer() {
        return group != null;
    }

    public Refund(CustomerGroup group) {
        this.group = group;
    }

    public Refund(CustomerGroup group, BankDetails details) {
        this.group = group;
        this.bankDetails = details;
    }

    public Refund(RoomUse roomUse) {
        this(roomUse, false);
    }

    public Refund(RoomUse roomUse, boolean forCustomer) {
        if (forCustomer) {
            this.group = roomUse.getCustomerGroup();
        } else {
            this.roomUse = roomUse;
        }
    }

    public Refund(RoomUse roomUse, boolean forCustomer, BankDetails details) {
        this(roomUse, forCustomer);
        this.bankDetails = details;
    }
}