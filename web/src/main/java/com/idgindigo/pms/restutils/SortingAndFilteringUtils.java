package com.idgindigo.pms.restutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.repository.filtering.Comparison;
import com.idgindigo.pms.repository.filtering.Comparisons;
import com.idgindigo.pms.repository.filtering.FilterParameter;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.idgindigo.pms.repository.filtering.Comparisons.IS_EMPTY_LIST;
import static com.idgindigo.pms.repository.filtering.Comparisons.IS_NULL;

/**
 * @author valentyn_vakatsiienko
 * @since 11/13/13 9:58 AM
 */
public class SortingAndFilteringUtils {

    public static Sort parseSortString(String sortString) throws IOException {
        Sort sort = null;
        if (sortString != null) {
            List<OrderDto> sortOrderList = Arrays.asList(new ObjectMapper().readValue(sortString, OrderDto[].class));
            sort = new Sort(convertOrderDtoList(sortOrderList));
        }
        return sort;
    }

    public static Collection<FilterParameter> parseFilterString(Map<String, String[]> parameters) {
        if (parameters.containsKey("filter[0][field]")) {
            Collection<FilterParameter> filterParameters = new ArrayList<FilterParameter>();
            for (int i = 0; parameters.containsKey("filter[" + i + "][field]"); i++) {
                String fieldParam = "filter[" + i + "][field]";
                String field = parameters.containsKey(fieldParam) ? parameters.get(fieldParam)[0] : null;

                String typeParam = "filter[" + i + "][data][type]";
                String type = parameters.containsKey(typeParam) ? parameters.get(typeParam)[0] : null;

                String comparisonParam = "filter[" + i + "][comparison]";
                String comparison = parameters.containsKey(comparisonParam) ? parameters.get(comparisonParam)[0] : Comparisons.EMPTY.name();

                String[] values;
                List<String> valueList = new ArrayList<>();
                if (parameters.containsKey("filter[" + i + "][data][value][0]")) {
                    for (int j = 0; parameters.containsKey("filter[" + i + "][data][value][" + j + "]"); j++) {
                        valueList.add(parameters.get("filter[" + i + "][data][value][" + j + "]")[0]);
                    }
                    values = valueList.toArray(new String[valueList.size()]);
                } else {
                    values = parameters.get("filter[" + i + "][data][value]");
                }
                validateFilterParams(field, type, values, comparison);

                filterParameters.add(new FilterParameter(field, type, values, comparison));
            }
            return filterParameters;
        } else {
            return Collections.emptyList();
        }
    }

    private static void validateFilterParams(String field, String type, String[] values, String comparisonString) {
        Comparison comparison = Comparisons.valueOf(comparisonString.toUpperCase());
        RestFriendlyException invalidParams = new RestFriendlyException(RestFriendlyException.ERROR_INVALID_FILTER_PARAMETERS);

        if (comparison != IS_EMPTY_LIST && comparison != IS_NULL) {
            if (type == null || values == null || values.length == 0) {
                throw invalidParams;
            }
        }
        if (field == null) {
            throw invalidParams;
        }
    }

    private static List<Sort.Order> convertOrderDtoList(Collection<OrderDto> orderDtoList) {
        List<Sort.Order> result = new ArrayList<>(orderDtoList.size());
        for (OrderDto orderDto : orderDtoList) {
            result.add(new Sort.Order(Sort.Direction.fromStringOrNull(orderDto.getDirection()), orderDto.getProperty()));
        }
        return result;
    }
}
