package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.BaseEntity;
import org.springframework.stereotype.Controller;

/**
 * @author valentyn_vakatsiienko
 * @since 11/12/13 5:37 PM
 */
@Controller
public interface FilteringController<T extends BaseEntity> {


//    @RequestMapping
//    @ResponseBody
//    @ResponseView(BaseEntity.ListView.class)
//    ResponseEntity<List<T>> listFiltered(@RequestParam(value = BaseCrudController.PAGE, defaultValue = "1") Integer page,
//                                         @RequestParam(value = BaseCrudController.START, defaultValue = "0") Integer start,
//                                         @RequestParam(value = BaseCrudController.LIMIT, defaultValue = BaseCrudController.DEFAULT_RECORDS_ON_PAGE) Integer limit,
//                                         @RequestParam(value = BaseCrudController.SORT, required = false) String sortString,
//                                         @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective,
//                                         HttpServletRequest request) throws Exception;
}
