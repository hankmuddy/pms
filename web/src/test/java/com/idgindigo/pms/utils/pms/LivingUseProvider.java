package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/24/14 4:14 PM
 */
@Component
public class LivingUseProvider extends ApprovableEntityProvider<LivingUse> {
    @Inject
    private LivingUseRepository repository;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Override
    public LivingUse createAndFill() {
        final LivingUse livingUse = new LivingUse();
        livingUse.setDate(new LocalDate());
        livingUse.setRoomUse(roomUseProvider.getPersistentEntity());
        livingUse.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setRoomUse(livingUse.getRoomUse());
            }
        }));
        livingUse.setService(livingProvider.getPersistentEntity());
        livingUse.setQuantity(1);
        livingUse.setTotal(livingUse.getService().getMon() * livingUse.getQuantity());
        livingUse.setRawTotal(livingUse.getTotal());
        return livingUse;
    }

    @Override
    public BaseRepository<LivingUse> getRepository() {
        return repository;
    }
}
