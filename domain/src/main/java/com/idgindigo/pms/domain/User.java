package com.idgindigo.pms.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author vomel
 * @since 29.10.13 13:23
 */
@Entity(name = "application_user")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User extends BaseEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "role", insertable = true, updatable = false)
    private Role role;

    @NotNull
    @Column(unique = true, updatable = false)
    private String username;

    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String patronymic;
    private String address;
    @NotNull
    private String phone;
    private String position;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        Identifiable other = (Identifiable) o;
        return null != getId() && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (int) (31 * result + (getId() != null ? getId() : 0));
        return result;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("role", role.getName())
                .append("email", username)
                .toString();
    }
}
