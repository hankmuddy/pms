package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.Approvable;
import com.idgindigo.pms.repository.ApprovableRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author vomel
 * @since 17.03.14 18:19
 */
@NoRepositoryBean
public interface WubookRoomRepository<T extends Approvable> extends ApprovableRepository<T> {
    T findByShortname(String shortname);

    T findByRid(Long rid);

    List<T> findByRidNullAndApprovedTrue();
}
