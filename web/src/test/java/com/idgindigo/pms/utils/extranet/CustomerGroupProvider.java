package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.idgindigo.pms.domain.extranet.CustomerGroup.PurposeOfVisit.TOURISM;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:59 PM
 */
@Component
public class CustomerGroupProvider extends ApprovableEntityProvider<CustomerGroup> {
    @Inject
    private AdultProvider adultProvider;
    @Inject
    private CustomerGroupRepository repository;

    @Override
    public CustomerGroup createAndFill() {
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setCustomer(adultProvider.getPersistentEntity());
        customerGroup.setPov(TOURISM);
        return customerGroup;
    }

    @Override
    public BaseRepository<CustomerGroup> getRepository() {
        return repository;
    }
}
