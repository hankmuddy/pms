package com.idgindigo.pms.repository.filtering;

import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author avoinovan
 * @since 8/6/13 4:48 PM
 */
public interface FilteredRepository<T> {

    Page<T> repoFilter(Iterable<FilterParameter> filterParameters, String connective, Pageable pageable) throws ParseException, IOException;

    BaseRepository<T> getRepository();

}
