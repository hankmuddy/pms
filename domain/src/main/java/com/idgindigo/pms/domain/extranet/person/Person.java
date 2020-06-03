package com.idgindigo.pms.domain.extranet.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 1:40 PM
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Child.class, name = Child.CHILD),
        @JsonSubTypes.Type(value = Adult.class, name = Adult.ADULT)})
@Getter
@Setter
public abstract class Person extends BaseEntity {
    public static final String PERSON = "person";

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String patronymic;
    private LocalDate dob;
    private String identity;

    @Column(name = "type", insertable = false, updatable = false)
    @JsonProperty("type")
    private String type;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .toString();
    }
}
