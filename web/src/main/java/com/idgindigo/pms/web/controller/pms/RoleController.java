package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.Role;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.RoleRepository;
import com.idgindigo.pms.web.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 4/11/14 12:03 PM
 */
@Controller
@RequestMapping(RoleController.URL)
public class RoleController extends BaseCrudController<Role> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Role.ROLE;
    @Inject
    private RoleRepository repository;

    @Override
    public BaseRepository<Role> getRepository() {
        return repository;
    }
}
