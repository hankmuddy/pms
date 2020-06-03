package com.idgindigo.pms.repository.pms;


import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

/**
 * @author vomel
 * @since 31.10.13 17:01
 */
public interface AccommodationRepository extends ApprovableRepository<Accommodation>, FilteredRepository<Accommodation> {
}
