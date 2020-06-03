package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.web.controller.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/12/13 5:43 PM
 */
public interface FilteringService<T extends BaseIdentifiable> {
    ResponseEntity<List<T>> listFiltered(Integer page, Integer start, Integer limit, String sortString, String connective,
                                         HttpServletRequest request) throws IOException, ParseException;

    FilteredRepository<T> getRepository();

}
