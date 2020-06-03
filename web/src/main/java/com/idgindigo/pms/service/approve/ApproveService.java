package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.repository.ApprovableRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:19 PM
 */
public interface ApproveService<T extends ApprovableEntity> {
    T approve(Long id);

    ApprovableRepository<T> getApproveRepository();
}
