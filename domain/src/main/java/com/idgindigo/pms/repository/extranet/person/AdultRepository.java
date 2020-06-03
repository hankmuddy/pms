package com.idgindigo.pms.repository.extranet.person;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.DiscountRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Component;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:08 PM
 */
@Component
public interface AdultRepository extends BaseRepository<Adult>, DiscountRepository, FilteredRepository<Adult> {
    Adult findByEmail(String email);
}
