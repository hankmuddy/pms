package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.DiscountRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 12/16/13 12:28 PM
 */
public interface CompanyRepository extends BaseRepository<Company>, DiscountRepository, FilteredRepository<Company> {
}
