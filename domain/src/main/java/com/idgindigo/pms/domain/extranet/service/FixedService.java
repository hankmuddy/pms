package com.idgindigo.pms.domain.extranet.service;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 6:23 PM
 */
@Entity
@Getter
@Setter
public abstract class FixedService extends Service {
    @NotNull
    @Column(unique = true)
    private String title;
    @NotNull
    private Long price;
}
