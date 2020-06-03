package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.ExtranetRoomUseRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.utils.extranet.ExtranetRoomUseProvider;
import mockit.Injectable;
import mockit.Verifications;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Collections;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_FREE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;

/**
 * @author valentyn_vakatsiienko
 * @since 6/19/14 4:14 PM
 */
public class ExtranetRoomUseServiceTest extends ServiceTest {
    @Inject
    private ExtranetRoomUseService service;
    @Inject
    private ExtranetRoomUseProvider provider;
    @Inject @Injectable
    private QuotaService quotaService;
    @Inject
    private ExtranetRoomUseRepository repository;

    @Test
    public void testCreate() {
        final ExtranetRoomUse created = service.create(provider.getTransientEntity(), Collections.<LocalDate, Long>emptyMap());
        new Verifications() {{
            quotaService.updateQuota(created.getStartDate(), created.getEndDate(), created.getBaseRoom().roomType(), -1, false);
        }};
        Assert.assertEquals(created.getStatus(), BOOKING_FREE);
        Assert.assertNotEquals(created.getTotal(), 0);
        Assert.assertEquals(created.getTotal(), created.getCustomerGroup().getTotal().longValue());
    }

    @Test
    public void testRefuse() {
        ExtranetRoomUse roomUse = provider.getPersistentEntity();
        ExtranetRoomUse refused = service.refuse(roomUse);
        Assert.assertEquals(refused.getStatus(), REFUSE);
        Assert.assertEquals(repository.getStatus(refused.getId()), REFUSE);
    }
}
