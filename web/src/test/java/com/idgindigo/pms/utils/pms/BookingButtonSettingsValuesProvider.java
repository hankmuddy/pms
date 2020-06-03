package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.logins.domain.BookingButtonSettingsValues;
import com.idgindigo.pms.logins.repository.BookingButtonSettingsValuesRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 05.06.14 13:12
 */
@Component
public class BookingButtonSettingsValuesProvider extends EntityProvider<BookingButtonSettingsValues> {

    @Inject
    private BookingButtonSettingsValuesRepository repository;
    @Inject
    private BookingButtonSettingsProvider bbsProvider;

    @Override
    public BookingButtonSettingsValues createAndFill() {
        BookingButtonSettingsValues bbsv = new BookingButtonSettingsValues();
        bbsv.setBbs(bbsProvider.getFirst());
        bbsv.setKey(randomAlphabeticString());
        bbsv.setValue(randomAlphabeticString());
        return bbsv;
    }

    @Override
    public BaseRepository<BookingButtonSettingsValues> getRepository() {
        return repository;
    }
}
