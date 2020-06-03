package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.restutils.SortingAndFilteringUtils;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.NotFilteringService;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.idgindigo.pms.domain.BaseEntity.ListView;
import static com.idgindigo.pms.domain.BaseEntity.SoloView;

/**
 * @author vomel
 * @since 29.10.13 15:33
 */
@Controller
public abstract class BaseCrudController<T extends BaseIdentifiable> {
    private static int MAX_PAGE_SIZE = 10000;//TODO find out the optimal value
    public static final String DEFAULT_RECORDS_ON_PAGE = "100";
    public static final String START = "start";
    public static final String LIMIT = "limit";
    public static final String SORT = "sort";
    public static final String PAGE = "page";
    public static final String SHOW_DELETED = "showDeleted";
    public static final String CONNECTIVE = "connective";
    public static final String ID = "{id}";

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Inject
    private NotFilteringService filteringService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Transactional
    public ResponseEntity<T> create(@RequestBody T entity) {
        entity.setId(null);
        T save = getRepository().save(entity);
        return new ResponseEntity<>(save);
    }

    @RequestMapping(method = RequestMethod.GET, params = "!filter[0][field]")
    @ResponseBody
    @ResponseView(ListView.class)
    @Transactional
    public ResponseEntity<List<T>> list(@RequestParam(value = PAGE, defaultValue = "1") Integer page,
                                        @RequestParam(value = START, defaultValue = "0") Integer start,
                                        @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit,
                                        @RequestParam(value = SORT, required = false) String sortString,
                                        @RequestParam(value = SHOW_DELETED, defaultValue = "false") boolean showDeleted) throws IOException {
        if (showDeleted) {
            entityManager.unwrap(Session.class).disableFilter(BaseIdentifiable.ACTIVE_FILTER);
        }
        Pageable pageable = getPageable(page, start, limit > 0 ? limit : MAX_PAGE_SIZE, sortString);
        Page<T> result = getRepository().findAll(pageable);
        return getListResponseEntity(result);
    }

    public static <X extends BaseIdentifiable> ResponseEntity<List<X>> getListResponseEntity(Page<X> result) {
        return new ResponseEntity<>(result.getContent(), new PageWithTotalCount(result.getNumber(), result.getSize(), result.getSort(), result.getTotalElements()));
    }

    public static Pageable getPageable(Integer page, Integer start, Integer limit, String sortString) throws IOException {
        Sort sort = SortingAndFilteringUtils.parseSortString(sortString);
        return new PageWithTotalCount(page - 1, limit, start, sort);
    }

    @RequestMapping(value = ID, method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(SoloView.class)
    @Transactional
    public ResponseEntity<T> getById(@PathVariable("id") Long id) {
        T entity = getRepository().findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return new ResponseEntity<T>(entity);
    }

    @RequestMapping(value = ID, method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(SoloView.class)
    @Transactional
    public ResponseEntity<T> update(@RequestBody T entity, @PathVariable("id") Long id) {
        if (!getRepository().exists(id)) {
            throw new EntityNotFoundException();
        }
        entity.setId(id);
        return new ResponseEntity<>(getRepository().save(entity));
    }

    @RequestMapping(value = ID, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!isDeletable()) {
            throw new EntityNotFoundException();
        }
        getRepository().delete(id);
        return new ResponseEntity<>();
    }

    protected boolean isDeletable() {
        return true;
    }

    @RequestMapping
    @ResponseBody
    @ResponseView(ListView.class)
    public ResponseEntity<List<T>> listFiltered(
            @RequestParam(value = PAGE, defaultValue = "1") Integer page,
            @RequestParam(value = START, defaultValue = "0") Integer start,
            @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit,
            @RequestParam(value = SORT, required = false) String sortString,
            @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        return getFilteringService().listFiltered(page, start, limit > 0 ? limit : MAX_PAGE_SIZE, sortString, connective, request);
    }

    @SuppressWarnings("unchecked")
    protected FilteringService<T> getFilteringService() {
        return filteringService;
    }

    public abstract BaseRepository<T> getRepository();

}
