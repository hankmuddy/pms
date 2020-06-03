package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 6:24 PM
 */
public class LivingControllerTest extends BaseWebCrudTest<Living> {
    @Inject
    private LivingProvider provider;

    @Override
    protected EntityProvider<Living> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return LivingController.URL + "/";
    }
}
