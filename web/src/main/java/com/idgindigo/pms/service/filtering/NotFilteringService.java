package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/14/14 1:14 PM
 */
@Service
public class NotFilteringService implements FilteringService {
    @Override
    public ResponseEntity<List> listFiltered(Integer page, Integer start, Integer limit, String sortString, String connective, HttpServletRequest request) throws IOException, ParseException {
        throw new EntityNotFoundException();
    }

    @Override
    public FilteredRepository getRepository() {
        throw new UnsupportedOperationException();
    }
}
