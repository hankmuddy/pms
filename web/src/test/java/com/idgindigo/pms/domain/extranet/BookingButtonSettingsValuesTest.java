package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.logins.domain.BookingButtonSettingsValues;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BookingButtonSettingsValuesProvider;

import javax.inject.Inject;

public class BookingButtonSettingsValuesTest extends BasePersistenceTest<BookingButtonSettingsValues> {
    @Inject
    private BookingButtonSettingsValuesProvider provider;

    @Override
    protected EntityProvider<BookingButtonSettingsValues> getProvider() {
        return provider;
    }
}