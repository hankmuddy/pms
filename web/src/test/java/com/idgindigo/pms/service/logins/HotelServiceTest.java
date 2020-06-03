package com.idgindigo.pms.service.logins;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.admin.HotelService;
import com.idgindigo.pms.utils.HotelInfoProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 5/19/14 11:36 AM
 */
public class HotelServiceTest extends ServiceTest {
    @Inject
    private HotelProvider provider;
    @Inject
    private HotelInfoProvider infoProvider;
    @Inject
    private HotelRepository repository;
    @Inject
    private HotelService service;

    @Test
    public void testBlocking() {
        final LocalDate now = LocalDate.now(DateTimeZone.UTC);

        Hotel tomorrow = getHotel(now);

        Hotel blocked = getHotel(now.minusDays(1));
        repository.setBlocked(blocked.getId(), true);

        Hotel toBlock = getHotel(now.minusDays(1));

        service.manageBlocking();
        assertTrue(repository.findOne(toBlock.getId()).isBlocked());
        assertTrue(repository.findOne(blocked.getId()).isBlocked());
        assertFalse(repository.findOne(tomorrow.getId()).isBlocked());
    }

    public Hotel getHotel(final LocalDate paidTill) {
        return provider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setPaidUntil(paidTill);
                entity.setInfo(infoProvider.getTransientEntity(new Visitor<HotelInfo>() {
                    @Override
                    public void visit(HotelInfo entity) {
                        entity.setTimeZone(DateTimeZone.UTC.getID());
                    }
                }));
            }
        });
    }
}
