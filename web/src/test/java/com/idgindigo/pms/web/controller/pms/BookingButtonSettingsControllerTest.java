package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BookingButtonSettingsProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import com.idgindigo.pms.web.controller.admin.BookingButtonSettingsController;

import javax.inject.Inject;

public class BookingButtonSettingsControllerTest extends BaseWebCrudTest<BookingButtonSettings> {
    @Inject
    private BookingButtonSettingsProvider provider;

    @Override
    protected EntityProvider<BookingButtonSettings> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return BookingButtonSettingsController.URL + "/";
    }

}