package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.Repair;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.RepairProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import lombok.Getter;
import lombok.Setter;
import mockit.Mocked;
import mockit.Verifications;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 2:18 PM
 */
public class RepairControllerTest extends BaseWebCrudTest<Repair> {
    @Inject
    private RepairProvider provider;
    @Inject
    private RepairController repairController;
    @Inject
    private BaseRoomUseRepository baseRoomUseRepository;
    @Mocked
    private RoomUseService roomUseService;
    @Mocked
    private QuotaService quotaService;

    @Test
    public void testAvailabilityIsBeingConfirmed() throws Exception {
        //Test create
        final Repair create = create();
        verifyAvailabilityConfirmation(create, create.getStartDate(), create.getEndDate());

        //Test update with widen from left
        UpdateDto updateDto = new UpdateDto().invoke(true);
        verifyAvailabilityConfirmation(updateDto.getUpdate(), updateDto.getNewDate(), updateDto.getOldDate());

        //Test update with widen from right
        updateDto = new UpdateDto().invoke(false);
        verifyAvailabilityConfirmation(updateDto.getUpdate(), updateDto.getOldDate(), updateDto.getNewDate());
    }

    private Repair create() {
        final Repair create = provider.getTransientEntity();
        repairController.create(create);
        return create;
    }

    private void verifyAvailabilityConfirmation(final Repair create, final LocalDate startDate, final LocalDate endDate) {
        new Verifications() {{
            roomUseService.confirmAvailability(create, startDate, endDate);
        }};
    }

    @Test
    public void testQuotaManagement() {
        //Test create
        Repair repair = create();
        verifyUpdateQuota(repair);

        //Test update
        UpdateDto updateDto = new UpdateDto().invoke(true);
        verifyManageQuota(updateDto.getUpdate());

        updateDto = new UpdateDto().invoke(false);
        verifyManageQuota(updateDto.getUpdate());
    }

    private void verifyManageQuota(Repair update) {
        quotaService.manageQuota(baseRoomUseRepository.findOne(update.getId()), update, update.getRoom().getRoomType());
    }

    private void verifyUpdateQuota(final Repair repair) {
        new Verifications() {{
            quotaService.updateQuota(repair.getStartDate(), repair.getEndDate(), repair.getRoom().getRoomType(), -1, true);
        }};
    }

    @Override
    protected EntityProvider<Repair> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return RepairController.URL + "/";
    }

    @Getter
    @Setter
    private class UpdateDto {
        private Repair update;
        private LocalDate oldDate;
        private LocalDate newDate;

        public UpdateDto invoke(boolean fromLeft) {
            update = provider.getPersistentEntity();
            if (fromLeft) {
                oldDate = update.getStartDate();
                newDate = oldDate.minusDays(5);
                update.setStartDate(newDate);
            } else {
                oldDate = update.getEndDate();
                newDate = oldDate.plusDays(5);
                update.setEndDate(newDate);
            }

            repairController.update(update, update.getId());
            return this;
        }
    }
}
