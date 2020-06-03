package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.AccommodationProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 31.10.13 21:15
 */
public class AccommodationControllerTest extends ApprovableControllerTest<Accommodation> {
    @Inject
    private AccommodationProvider provider;

    @Override
    protected EntityProvider<Accommodation> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return AccommodationController.URL + "/";
    }
}
