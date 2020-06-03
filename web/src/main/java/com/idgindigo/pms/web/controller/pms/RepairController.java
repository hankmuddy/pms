package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Repair;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.RepairRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 2:13 PM
 */
@Controller
@RequestMapping(RepairController.URL)
public class RepairController extends BaseCrudController<Repair> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Repair.REPAIR;

    @Inject
    private RepairRepository repository;
    @Inject
    private QuotaService quotaService;
    @Inject
    private RoomRepository roomRepository;
    @Inject
    private RoomUseService roomUseService;

    @Override
    @Transactional
    public ResponseEntity<Repair> create(@RequestBody Repair entity) {
        roomUseService.confirmAvailability(entity, entity.getStartDate(), entity.getEndDate());

        ResponseEntity<Repair> repairResponseEntity = super.create(entity);
        RoomType roomType = roomRepository.findOne(entity.getRoom().getId()).getRoomType();
        quotaService.updateQuota(entity.getStartDate(), entity.getEndDate(), roomType, -1, true);
        return repairResponseEntity;
    }

    @Override
    @Transactional
    public ResponseEntity<Repair> update(@RequestBody Repair entity, @PathVariable("id") Long id) {
        Repair old = repository.findOne(id);
        Repair oldCopy = new Repair();
        BeanUtils.copyProperties(old, oldCopy, "id");
        if (entity.getEndDate().isAfter(old.getEndDate())) {
            roomUseService.confirmAvailability(entity, old.getEndDate(), entity.getEndDate());
        }
        if (entity.getStartDate().isBefore(old.getStartDate())) {
            roomUseService.confirmAvailability(entity, entity.getStartDate(), old.getStartDate());
        }
        ResponseEntity<Repair> updatedRepair = super.update(entity, id);
        quotaService.manageQuota(oldCopy, entity, old.getRoom().getRoomType());
        return updatedRepair;
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Repair repair = repository.findOne(id);
        ResponseEntity<?> delete = super.delete(id);
        quotaService.updateQuota(repair.getStartDate(), repair.getEndDate(), repair.getRoom().getRoomType(), 1, true);
        return delete;
    }

    @Override
    public BaseRepository<Repair> getRepository() {
        return repository;
    }
}
