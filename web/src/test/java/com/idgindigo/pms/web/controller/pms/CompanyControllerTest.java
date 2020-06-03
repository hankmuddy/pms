package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.CompanyProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:52 PM
 */
public class CompanyControllerTest extends BaseWebCrudTest<Company> {
    @Inject
    private CompanyProvider provider;

    @Override
    protected EntityProvider<Company> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return CompanyController.URL + "/";
    }
}
