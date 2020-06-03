package com.idgindigo.pms.domain.extranet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 3:04 PM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class GroupMember extends BaseEntity {
    public static final String GROUP_MEMBER = "groupMember";
    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private CustomerGroup customerGroup;

    @OneToMany(mappedBy = GROUP_MEMBER)
    @JsonIgnore
    private List<GroupMemberToRoomUse> groupMemberToRoomUses = new ArrayList<>();

    public GroupMember(Person person, CustomerGroup group) {
        this.person = person;
        this.customerGroup = group;
    }

}
