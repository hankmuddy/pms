package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 3:05 PM
 */
@Entity
@Getter
@Setter
@Check(constraints = "(discount between 0 and rawTotal and totalPaid <= total)" +
        " and " +
        "(roomUse_id IS NULL OR group_id IS NULL) AND (roomUse_id IS NOT NULL OR group_id IS NOT NULL)")
public class Bill extends ApprovableEntity {
    public static final String BILL = "bill";

    @ManyToOne
    @JoinColumn(updatable = false)
    private RoomUse roomUse;
    @ManyToOne
    @JoinColumn(updatable = false)
    private CustomerGroup group;
    @OneToMany(mappedBy = BILL)
    @JsonView(BillView.class)
    private Set<BaseServiceUse> serviceUses = new HashSet<>();
    @NotNull
    @Column(updatable = false, nullable = false)
    private Long rawTotal = 0L;
    @NotNull
    @Column(updatable = false, nullable = false)
    private Long total = 0L;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    @Column(updatable = false, nullable = false)
    private long totalPaid = 0L;
    @JsonView({SoloView.class, BillView.class})
    private String description;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    @Column(updatable = false, nullable = false)
    private Long discount = 0L;

    public boolean isForCustomer() {
        return group != null;
    }

    @JsonIgnore
    public boolean isFullyPaid() {
        return total == totalPaid;
    }

    public interface BillView extends BaseView {
    }

}
