package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Корпус
 *
 * @author vomel
 * @since 31.10.13 16:54
 */
@Entity
@Getter
@Setter
public class Accommodation extends ApprovableEntity {
    public static final String ACCOMMODATION = "accommodation";
    @NotNull
    private String name;
    @NotNull
    private String shortName;
    @JsonIgnore
    @OneToMany(mappedBy = ACCOMMODATION)
    private List<Room> rooms;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("approved", getApproved())
                .append("name", name)
                .toString();
    }
}
