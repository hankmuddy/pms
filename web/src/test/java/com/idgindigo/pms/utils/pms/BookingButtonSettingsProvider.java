package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.logins.domain.BookingButtonSettingsValues;
import com.idgindigo.pms.logins.repository.BookingButtonSettingsRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.HotelProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vomel
 * @since 27.05.14 14:48
 */
@Component
public class BookingButtonSettingsProvider extends EntityProvider<BookingButtonSettings> {
    @Inject
    private BookingButtonSettingsRepository repository;
    @Inject
    private HotelProvider hotelProvider;

    @Override
    public BookingButtonSettings createAndFill() {
        BookingButtonSettings bbs = new BookingButtonSettings();
        bbs.setCurrency(randomAlphabeticString(3));
        bbs.setTextColor(randomAlphabeticString(6));
        bbs.setLanguage(randomAlphabeticString(2));
        bbs.setName("name: " + randomAlphabeticString(8));
        bbs.setBackgroundColor(randomAlphabeticString(6));
        bbs.setHotel(hotelProvider.getFirst());
        addKeyValues(bbs);
        return bbs;
    }

    public static void addKeyValues(final BookingButtonSettings bbs) {
        List<BookingButtonSettingsValues> keyValues = new ArrayList<BookingButtonSettingsValues>() {{
            for (int i = 0; i < 4; i++) {
                BookingButtonSettingsValues bbsv = new BookingButtonSettingsValues();
                bbsv.setKey("key:" + randomAlphabeticString(4));
                bbsv.setValue("value" + randomAlphabeticString(4));
                add(bbsv);
            }
        }};
        bbs.setKeyValues(keyValues);
    }

    @Override
    public void updateEntity(BookingButtonSettings entity) {
        super.updateEntity(entity);
        addKeyValues(entity);
    }

    @Override
    public BaseRepository<BookingButtonSettings> getRepository() {
        return repository;
    }
}
