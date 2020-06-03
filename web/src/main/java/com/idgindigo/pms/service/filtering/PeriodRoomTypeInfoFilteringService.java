package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.PeriodRoomTypeInfoRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/25/14 10:36 AM
 */
@Service
public class PeriodRoomTypeInfoFilteringService extends GenericFilteringService<PeriodRoomTypeInfo> {
    @Inject
    private PeriodRoomTypeInfoRepository repository;

    @Override
    public FilteredRepository<PeriodRoomTypeInfo> getRepository() {
        return repository;
    }
}
