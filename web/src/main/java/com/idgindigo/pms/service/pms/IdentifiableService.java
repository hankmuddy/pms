package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.stereotype.Service;

/**
 * @author valentyn_vakatsiienko
 * @since 12/16/13 1:03 PM
 */
@Service
public class IdentifiableService {

    public <T extends BaseIdentifiable> T handleCascade(T entity, BaseRepository<T> repository) {
        return entity;
    }
}
