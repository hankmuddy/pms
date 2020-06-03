package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 10:56 AM
 */
@Entity
@Getter
@Setter
public class CompanyContact extends BaseEntity {
    public static final String COMPANY_CONTACT = "companyContact";

    @ManyToOne
    @NotNull
    private Company company;
    @NotNull
    private String name;
    private String email;
    private String phone;
    private String description;
    private String post;
}
