package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.CompanyRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:48 PM
 */
@Component
public class CompanyProvider extends EntityProvider<Company> {
    @Inject
    private CompanyRepository repository;

    @Override
    public Company createAndFill() {
        Company company = new Company();
        company.setName(randomString());
        return company;
    }

    @Override
    public BaseRepository<Company> getRepository() {
        return repository;
    }
}
