package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 4:16 PM
 */
public interface ChildRepository extends BaseRepository<Child>, FilteredRepository<Child> {
}
