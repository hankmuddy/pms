package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Repair;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.RepairRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 2:14 PM
 */
@Component
public class RepairProvider extends EntityProvider<Repair> {
    @Inject
    private RepairRepository repository;
    @Inject
    private RoomProvider roomProvider;

    @Override
    public Repair createAndFill() {
        Repair repair = new Repair();
        repair.setStartDate(new LocalDate());
        repair.setEndDate(repair.getStartDate().plusDays(1));
        repair.setRoom(roomProvider.getPersistentEntity());
        return repair;
    }

    @Override
    public BaseRepository<Repair> getRepository() {
        return repository;
    }
}
