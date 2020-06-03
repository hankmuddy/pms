package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:42 AM
 */
@Component
public class BillProvider extends ApprovableEntityProvider<Bill> {
    @Inject
    private BillRepository repository;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Override
    public Bill createAndFill() {
        Bill bill = new Bill();
        bill.setRoomUse(roomUseProvider.getPersistentEntity());
        bill.setRawTotal(randomPositiveLong());
        bill.setTotal(bill.getRawTotal());
        return bill;
    }

    @Override
    public BaseRepository<Bill> getRepository() {
        return repository;
    }
}
