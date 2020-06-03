package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.extranet.person.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 1:05 PM
 */
@Entity
@Getter
@Setter
@DiscriminatorValue(Child.CHILD)
public class Child extends Person {
    public static final String CHILD = "child";

    @Override
    public String getType() {
        return CHILD;
    }

}
