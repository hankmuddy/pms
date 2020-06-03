package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.extranet.BaseRoomValueRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.BaseRoomValueProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.web.controller.extranet.BaseRoomValueController.PeriodDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 1:08 PM
 */
public class BaseRoomValueControllerTest extends BaseWebCrudTest<BaseRoomValue> {
    @Inject
    private BaseRoomValueProvider provider;
    @Inject
    private BaseRoomValueRepository repository;
    @Inject
    private BaseRoomValueController controller;

    @Test
    public void testPeriodCreate() throws Exception {
        BaseRoomValue value = provider.getTransientEntity();
        create(value.getRoom(), value.getDate(), 3);

        PeriodDto dto = new PeriodDto();
        BeanUtils.copyProperties(value, dto);
        dto.setDateStart(value.getDate());
        dto.setDateEnd(dto.getDateStart().plusDays(5));
        dto.setRooms(Collections.singletonList(value.getRoom()));

        mvc.perform(preparePost(dto, "period")).andExpect(status().isCreated());
        List<BaseRoomValue> res = repository.findByRoom(value.getRoom());
        assertEquals(res.size(), 6);
    }

    private void create(final BaseRoom room, LocalDate start, int total) {
        for (LocalDate date = start; !date.isAfter(start.plusDays(total - 1)); date = date.plusDays(1)) {
            final LocalDate now = date;
            provider.getPersistentEntity(new Visitor<BaseRoomValue>() {
                @Override
                public void visit(BaseRoomValue entity) {
                    entity.setRoom(room);
                    entity.setDate(now);
                }
            });
        }
    }

    @Override
    protected EntityProvider<BaseRoomValue> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return BaseRoomValueController.URL + "/";
    }
}
