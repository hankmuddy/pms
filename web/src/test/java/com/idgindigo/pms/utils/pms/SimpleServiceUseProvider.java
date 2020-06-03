package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import com.idgindigo.pms.utils.Visitor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:54 PM
 */
@Component
public class SimpleServiceUseProvider extends ApprovableEntityProvider<SimpleServiceUse> {
    public static final int QUANTITY = 50;
    @Inject
    private SimpleServiceUseRepository repository;
    @Inject
    private SimpleServiceProvider simpleServiceProvider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Override
    public SimpleServiceUse createAndFill() {
        final SimpleServiceUse serviceUse = new SimpleServiceUse();
        serviceUse.setDate(new LocalDate());
        serviceUse.setRoomUse(roomUseProvider.getPersistentEntity());
        serviceUse.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setRoomUse(serviceUse.getRoomUse());
            }
        }));
        serviceUse.setService(simpleServiceProvider.getPersistentEntity());
        serviceUse.setQuantity(QUANTITY);
        serviceUse.setTotal(serviceUse.getService().getPrice() * serviceUse.getQuantity());
        serviceUse.setRawTotal(serviceUse.getTotal());
        return serviceUse;
    }

    @Override
    public BaseRepository<SimpleServiceUse> getRepository() {
        return repository;
    }
}
