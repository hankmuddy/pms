package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.ChildProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 11:58 AM
 */
public class ChildControllerTest extends BaseWebCrudTest<Child> {
    @Inject
    private ChildProvider provider;

    @Override
    protected EntityProvider<Child> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return ChildController.URL + "/";
    }
}
