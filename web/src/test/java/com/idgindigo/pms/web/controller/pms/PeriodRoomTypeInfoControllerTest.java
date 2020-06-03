package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.PeriodRoomTypeInfoProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/25/14 10:38 AM
 */
public class PeriodRoomTypeInfoControllerTest extends BaseWebCrudTest<PeriodRoomTypeInfo> {
    @Inject
    private PeriodRoomTypeInfoProvider provider;

    @Override
    protected EntityProvider<PeriodRoomTypeInfo> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return PeriodRoomTypeInfoController.URL + "/";
    }
}