package com.idgindigo.pms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 10/31/13 4:44 PM
 */
@Entity
@Table(name = Role.ROLE)
@Getter
@Setter
@ToString(of = "name", callSuper = false)
public class Role extends BaseEntity {
    public static final String ROLE = "role";

    @Column(unique = true)
    private String name;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "RolePermission")
    @Column(name = "permission")
    private Set<Permission> permissions = EnumSet.noneOf(Permission.class);

}
