package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.CompanyContactRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 1:30 PM
 */
@Component
public class CompanyContactProvider extends EntityProvider<CompanyContact> {
    @Inject
    private CompanyContactRepository repository;
    @Inject
    private CompanyProvider companyProvider;

    @Override
    public CompanyContact createAndFill() {
        CompanyContact contact = new CompanyContact();
        contact.setCompany(companyProvider.getPersistentEntity());
        contact.setName(randomAlphabeticString());
        return contact;
    }

    @Override
    public BaseRepository<CompanyContact> getRepository() {
        return repository;
    }
}
