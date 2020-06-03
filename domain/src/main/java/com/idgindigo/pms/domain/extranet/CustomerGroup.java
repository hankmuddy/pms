package com.idgindigo.pms.domain.extranet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 2:41 PM
 */
@Entity
@Getter
@Setter
@Check(constraints = "customer_id is not null or company_id is not null")
public class CustomerGroup extends ApprovableEntity {
    public static final String GROUP = "customerGroup";

    @ManyToOne/*(cascade = CascadeType.PERSIST)*/
    @JoinColumn(updatable = false)
    private Adult customer;
    @ManyToOne/*(cascade = CascadeType.PERSIST)*/
    @JoinColumn(updatable = false)
    private Company company;
    @Column(updatable = false)
    @Max(100)
    @Min(0)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private int discount;
    @Column(updatable = false)
    private boolean includeCustomer;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private boolean closed;
    @Column(updatable = false, nullable = false)
    @Enumerated(STRING)
    private PurposeOfVisit pov;
    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Long total = 0L;
    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Long totalPaid = 0L;

    @OneToMany(mappedBy = GROUP)
    @JsonView(SoloView.class)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = GROUP)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<BaseGroupRoomUse> roomUses = new ArrayList<>();

    @JsonProperty("roomUsesQuantity")
    @JsonView(ListView.class)
    public int getRoomUsesQuantity() {
        return roomUses.size();
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .append("customer", customer)
                .append("company", company)
                .toString();
    }

    public enum PurposeOfVisit {
        TOURISM, BUSINESS
    }
}
