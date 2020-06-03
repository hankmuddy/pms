package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BookingButtonSettingsProvider;

import javax.inject.Inject;

public class BookingButtonSettingsTest extends BasePersistenceTest<BookingButtonSettings> {
    @Inject
    private BookingButtonSettingsProvider provider;

    @Override
    protected EntityProvider<BookingButtonSettings> getProvider() {
        return provider;
    }
}