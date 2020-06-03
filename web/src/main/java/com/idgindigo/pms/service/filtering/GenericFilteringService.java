package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.repository.filtering.FilterParameter;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.restutils.SortingAndFilteringUtils;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/13/13 10:18 AM
 */
public abstract class GenericFilteringService<T extends BaseIdentifiable> implements FilteringService<T> {

    @Override
    public ResponseEntity<List<T>> listFiltered(Integer page, Integer start, Integer limit, String sortString, String connective, HttpServletRequest request) throws IOException, ParseException {
        Iterable<FilterParameter> filterParameters = SortingAndFilteringUtils.parseFilterString(request.getParameterMap());

        Sort sort = SortingAndFilteringUtils.parseSortString(sortString);
        Pageable pageable = new PageWithTotalCount(page - 1, limit, start, sort);
        Page<T> result = getRepository().repoFilter(filterParameters, connective, pageable);
        return new ResponseEntity<>(result.getContent(), new PageWithTotalCount(result.getNumber(), result.getSize(), result.getSort(), result.getTotalElements()));
    }

}
