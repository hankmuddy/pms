package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.price.LivingPriceResolver;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.roomuse.ExtranetRoomUseRepository;
import com.idgindigo.pms.restutils.exception.ExtranetRoomUseException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.service.validation.GroupRoomUseValidationService;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Map;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 10:59 AM
 */
@Service
public class ExtranetRoomUseService {
    private static final Logger logger = LoggerFactory.getLogger(ExtranetRoomUseService.class);
    @Inject
    private ExtranetRoomUseRepository repository;
    @Inject
    private QuotaService quotaService;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private LivingPriceResolver priceResolver;
    @Inject
    private LivingService livingService;
    @Inject
    private GroupRoomUseValidationService groupRoomUseValidationService;
    @Inject
    private CustomerGroupRepository customerGroupRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ExtranetRoomUse create(@Valid ExtranetRoomUse entity, Map<LocalDate, Long> prices) {
        validateRoomUseCreate(entity);
        entity.setTotal(getTotal(entity, prices));

        //Room management
        entity = repository.save(entity);
        RoomType roomType = baseRoomRepository.findOne(entity.getBaseRoom().getId()).roomType();
        try {
            quotaService.updateQuota(entity.getStartDate(), entity.getEndDate(), roomType, -1, false);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Could not perform booking of {} for tenant: {}. Insufficient rooms", entity, SecurityUtils.getCurrentTenantId());
            throw new ExtranetRoomUseException(ExtranetRoomUseException.QUOTA_EXCEEDED);
        }

        CustomerGroup group = entity.getCustomerGroup();
        group.setTotal(group.getTotal() + entity.getTotal());
        customerGroupRepository.setTotal(group.getTotal(), group);
        return entity;
    }

    private void validateRoomUseCreate(ExtranetRoomUse entity) {
        groupRoomUseValidationService.validateRoomUseCreate(entity);
    }

    private long getTotal(ExtranetRoomUse entity, Map<LocalDate, Long> prices) {//TODO Tourism tax handling
        long res = 0;
        for (LocalDate date = entity.getStartDate(); date.isBefore(entity.getEndDate()); date = date.plusDays(1)) {
            if (prices.containsKey(date)) {
                res += prices.get(date);
            } else {
                Living living = livingService.get(entity.getBaseRoom(), entity.getPlan(), date);
                res += priceResolver.getPrice(living, date);
            }
        }
        return res;
    }

    public ExtranetRoomUse refuse(ExtranetRoomUse roomUse) {
        if (LocalDate.now(SecurityUtils.getTimeZone()).isAfter(roomUse.getStartDate())) {
            throw new RoomUseException(RoomUseException.REFUSE_INVALID_DATE, "dateStart");
        }
        repository.setStatus(REFUSE, roomUse.getId());
        roomUse.setStatus(REFUSE);
        return roomUse;
    }
}
