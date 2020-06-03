package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/28/13 2:54 PM
 */
@Entity
@DiscriminatorValue("REPAIR")
@Getter
@Setter
public class Repair extends BaseRoomUse implements PmsRoomUse {
    public static final String REPAIR = "repair";
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private Room room;

    @Override
    public UseType getType() {
        return UseType.REPAIR;
    }
}
