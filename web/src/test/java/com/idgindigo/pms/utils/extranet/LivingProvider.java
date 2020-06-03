package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 4:45 PM
 */
@Component
public class LivingProvider extends EntityProvider<Living> {
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private LivingRepository repository;

    @Override
    public Living createAndFill() {
        Living living = new Living();
        living.setRoom(virtualRoomProvider.getPersistentEntity());
        living.setPlan(planProvider.getPersistentEntity());
        living.setPrices(living.getRoom().getDefaultPrice());
        return living;
    }

    @Override
    public BaseRepository<Living> getRepository() {
        return repository;
    }
}
