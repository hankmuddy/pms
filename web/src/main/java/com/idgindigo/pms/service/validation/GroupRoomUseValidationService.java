package com.idgindigo.pms.service.validation;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.service.pms.CustomerGroupService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 11:18 AM
 */
@Service
public class GroupRoomUseValidationService {
    @Inject
    private ApprovedValidationService approvedValidationService;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private CustomerGroupService customerGroupService;
    @Inject
    private CustomerGroupRepository customerGroupRepository;

    public void validateRoomUseCreate(BaseGroupRoomUse roomUse) {
        approvedValidationService.validateApproved(roomUse.getPlan(), planRepository, new ApprovedValidationService.ExceptionGenerator() {
            @Override
            public RestFriendlyException generate() {
                return new RoomUseException(RoomUseException.PLAN_NOT_APPROVED, Plan.PLAN);
            }
        });
        approvedValidationService.validateApproved(roomUse.getBaseRoom(), baseRoomRepository, new ApprovedValidationService.ExceptionGenerator() {
            @Override
            public RestFriendlyException generate() {
                return new RoomUseException(RoomUseException.BASE_ROOM_NOT_APPROVED, BaseRoom.BASE_ROOM);
            }
        });

        //Validate dates
        if (roomUse.getStartDate().isEqual(roomUse.getEndDate())) {
            throw new RoomUseException(RoomUseException.INVALID_DATES, "endDate");
        }

        //Validate group
        CustomerGroup customerGroup = roomUse.getCustomerGroup();
        if (customerGroup.getId() == null) {
            roomUse.setCustomerGroup(customerGroupService.create(customerGroup));
        } else if (customerGroupRepository.isClosed(customerGroup.getId())) {
            throw new RoomUseException(RoomUseException.CLOSED_GROUP, CustomerGroup.GROUP);
        }

    }
}
