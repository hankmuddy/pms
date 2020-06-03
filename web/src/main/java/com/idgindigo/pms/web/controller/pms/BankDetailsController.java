package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.restutils.exception.BankDetailsException;
import com.idgindigo.pms.service.filtering.BankDetailsFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 12:54 PM
 */
@Controller
@RequestMapping(BankDetailsController.URL)
public class BankDetailsController extends BaseCrudController<BankDetails> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + BankDetails.BANK_DETAILS;
    @Inject
    private BankDetailsRepository repository;
    @Inject
    private BankDetailsFilteringService filteringService;

    @Override
    @Transactional
    public ResponseEntity<BankDetails> create(@RequestBody BankDetails entity) {
        if (repository.count() == 0) {
            entity.setDefaultDetails(true);
        }
        return super.create(entity);
    }

    @RequestMapping(value = "{id}/default", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public void setDefault(@PathVariable("id") long id) {
        repository.eraseDefaults();
        repository.setDefault(id);
    }

    @RequestMapping("default")
    @ResponseBody
    @Transactional
    public ResponseEntity<BankDetails> getDefault() {
        return new ResponseEntity<>(repository.getDefault());
    }

    @RequestMapping(value = "{id}/blocked", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public void block(@PathVariable("id") Long id, @RequestBody @Valid BlockedDto dto) {
        validateBlocked(id);
        repository.setBlocked(id, dto.blocked);
    }

    private void validateBlocked(Long id) {
        if (repository.isDefault(id)) {
            throw new BankDetailsException(BankDetailsException.BLOCK_DEFAULT_DETAILS, "defaultDetails");
        }
    }

    @Override
    protected FilteringService<BankDetails> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<BankDetails> getRepository() {
        return repository;
    }

    @Setter
    @Getter
    public static class BlockedDto {
        @NotNull
        private Boolean blocked;
    }
}
