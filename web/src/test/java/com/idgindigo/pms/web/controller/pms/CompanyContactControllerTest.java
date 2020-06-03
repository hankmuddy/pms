package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.CompanyContactProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 1:34 PM
 */
public class CompanyContactControllerTest extends BaseWebCrudTest<CompanyContact> {
    @Inject
    private CompanyContactProvider provider;

    @Override
    protected EntityProvider<CompanyContact> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return CompanyContactController.URL + "/";
    }
}
