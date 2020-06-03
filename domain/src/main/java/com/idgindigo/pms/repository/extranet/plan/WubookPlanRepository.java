package com.idgindigo.pms.repository.extranet.plan;

import com.idgindigo.pms.domain.Approvable;
import com.idgindigo.pms.repository.ApprovableRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author vomel
 * @since 25.06.14 16:21
 */
@NoRepositoryBean
public interface WubookPlanRepository<T extends Approvable> extends ApprovableRepository<T> {

    T findByPid(Long pid);

    List<T> findByPidNullAndApprovedTrue();

}
