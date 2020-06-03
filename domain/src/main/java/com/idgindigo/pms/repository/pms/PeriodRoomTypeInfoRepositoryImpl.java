package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/25/14 10:37 AM
 */
public class PeriodRoomTypeInfoRepositoryImpl extends AbstractFilteredRepository<PeriodRoomTypeInfo> {
    @Inject
    private PeriodRoomTypeInfoRepository repository;

    @Override
    public BaseRepository<PeriodRoomTypeInfo> getRepository() {
        return repository;
    }
}
