package com.idgindigo.pms.repository.filtering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author avoinovan
 * @since 8/6/13 5:23 PM
 */
public abstract class AbstractFilteredRepository<T> implements FilteredRepository<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFilteredRepository.class);

    @Override
    public Page<T> repoFilter(Iterable<FilterParameter> filterParameters, String connective, Pageable pageable) throws ParseException, IOException {
        try {
            return getRepository().findAll(getFilterSpecification(filterParameters, connective), pageable);
        } catch (Exception e) {
            logger.error("Filtering failed for parameters: {}, connective: {}", filterParameters, connective);
            throw e;
        }
    }

    private MultiFilterSpec<T> getFilterSpecification(Iterable<FilterParameter> filterParameters, String connective) {
        return new MultiFilterSpec<>(filterParameters, connective);
    }

}