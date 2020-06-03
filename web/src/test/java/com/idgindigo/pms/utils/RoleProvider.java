package com.idgindigo.pms.utils;

import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.domain.Role;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.RoleRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 10/31/13 5:22 PM
 */
@Component
public class RoleProvider extends EntityProvider<Role> {
    @Inject
    private RoleRepository repository;

    @Override
    public Role createAndFill() {
        Role role = new Role();
        role.setName(randomString());
        role.getPermissions().add(Permission.SETTINGS_SAVE);
        return role;
    }

    @Override
    public BaseRepository<Role> getRepository() {
        return repository;
    }

}
