package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 11:24 AM
 */
public interface CompanyContactRepository extends BaseRepository<CompanyContact>, FilteredRepository<CompanyContact> {
}
