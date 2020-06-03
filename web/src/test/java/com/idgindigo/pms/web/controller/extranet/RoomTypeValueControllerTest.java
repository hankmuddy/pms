package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 6:28 PM
 */
public class RoomTypeValueControllerTest extends BaseWebCrudTest<RoomTypeValue> {
    @Inject
    private RoomTypeValueProvider provider;
    @Inject
    private RoomTypeValueRepository repository;

    @Test
    public void testPeriodCreate() throws Exception {
        RoomTypeValue value = provider.getTransientEntity();
        create(value.getRoomType(), value.getDate(), 3);

        RoomTypeValueController.PeriodDto dto = new RoomTypeValueController.PeriodDto();
        BeanUtils.copyProperties(value, dto);
        dto.setDateStart(value.getDate());
        dto.setDateEnd(dto.getDateStart().plusDays(5));
        dto.setRoomTypes(Collections.singletonList(value.getRoomType()));

        mvc.perform(preparePost(dto, "period")).andExpect(status().isCreated());
        List<RoomTypeValue> res = repository.findByRoomTypeAndDateBetween(value.getRoomType(), dto.getDateStart(), dto.getDateEnd());
        assertEquals(res.size(), 6);
    }

    private void create(final RoomType room, LocalDate start, int total) {
        for (LocalDate date = start; !date.isAfter(start.plusDays(total - 1)); date = date.plusDays(1)) {
            final LocalDate now = date;
            provider.getPersistentEntity(new Visitor<RoomTypeValue>() {
                @Override
                public void visit(RoomTypeValue entity) {
                    entity.setRoomType(room);
                    entity.setDate(now);
                }
            });
        }
    }

    @Override
    protected EntityProvider<RoomTypeValue> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return RoomTypeValueController.URL + "/";
    }
}
