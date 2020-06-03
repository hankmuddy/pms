package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 5:37 PM
 */
public class PersonControllerTest extends BaseWebCrudTest<Adult> {
    @Inject
    private AdultProvider provider;

    @Override
    protected EntityProvider<Adult> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return AdultController.URL + "/";
    }
}
