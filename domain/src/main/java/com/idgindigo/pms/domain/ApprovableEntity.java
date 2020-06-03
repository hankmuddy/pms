package com.idgindigo.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 3:37 PM
 */
@Getter
@Setter
@MappedSuperclass
public class ApprovableEntity extends BaseEntity implements Approvable {
    @Column(updatable = false)
    @JsonIgnore
    private boolean approved = false;

    @JsonProperty("approved")
    public Boolean getApproved() {
        return approved;
    }

    @JsonIgnore
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

}
