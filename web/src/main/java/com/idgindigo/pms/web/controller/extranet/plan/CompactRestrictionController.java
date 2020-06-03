package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.plan.CompactRestriction;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.plan.CompactRestrictionRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:55 PM
 */
@Controller
@RequestMapping(CompactRestrictionController.URL)
public class CompactRestrictionController extends BaseCrudController<CompactRestriction> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + CompactRestriction.COMPACT_RESTRICTION;

    @Inject
    private CompactRestrictionRepository repository;
    @Inject
    private ChannelService channelService;

    @Override
    public ResponseEntity<CompactRestriction> create(@RequestBody CompactRestriction entity) {
        ResponseEntity<CompactRestriction> response = super.create(entity);
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.createCompactRestriction(response.getContent(), SecurityUtils.getWubookAccount());
        }
        return response;
    }

    @Override
    public ResponseEntity<CompactRestriction> update(@RequestBody CompactRestriction entity, @PathVariable("id") Long id) {
        ResponseEntity<CompactRestriction> response = super.update(entity, id);
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            response.getContent().setPid(repository.getPid(entity));
            channelService.updateRestrictionRules(response.getContent(), SecurityUtils.getWubookAccount());
        }
        return response;
    }

    @Override
    public BaseRepository<CompactRestriction> getRepository() {
        return repository;
    }
}
