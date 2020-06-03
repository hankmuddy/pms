package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.CompactRestriction;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.plan.CompactRestrictionProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:58 PM
 */
public class CompactRestrictionControllerTest extends BaseWebCrudTest<CompactRestriction> {
    @Inject
    private CompactRestrictionProvider provider;

    @Override
    protected EntityProvider<CompactRestriction> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return CompactRestrictionController.URL + "/";
    }
}
