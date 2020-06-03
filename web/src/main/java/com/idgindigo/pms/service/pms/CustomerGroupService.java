package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.Identifiable;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.extranet.person.PersonRepository;
import com.idgindigo.pms.repository.pms.CompanyRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.CustomerGroupException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/16/13 12:14 PM
 */
@Service
public class CustomerGroupService {
    public static final List<RoomUse.Status> OPEN_BOOKING_STATUSES = Arrays.asList(BaseGroupRoomUse.Status.BOOKING_FREE, BaseGroupRoomUse.Status.BOOKING_WARRANTY, BaseGroupRoomUse.Status.LIVING);

    @Inject
    private CustomerGroupRepository repository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private AdultRepository adultRepository;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private GroupMemberRepository groupMemberRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BillService billService;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private LivingUseService livingUseService;

    public CustomerGroup create(CustomerGroup group) {
        if (group.getCustomer() == null && group.getCompany() == null) {
            throw new CustomerGroupException(CustomerGroupException.CUSTOMER_AND_COMPANY_NULL, "customer,company");
        }
        if (group.getId() != null) {
            return group;
        }
        handleCustomer(group);
        group.setDiscount(getDiscount(group));
        group = repository.save(group);
        handleCreate(group);
        return group;
    }

    public void handleCustomer(CustomerGroup group) {
        Adult customer = group.getCustomer();
        if (customer != null && customer.getId() == null && customer.getEmail() != null) {
            Adult existing = adultRepository.findByEmail(customer.getEmail());
            if (existing != null) {
                group.setCustomer(existing);
            }
        }
    }

    private int getDiscount(CustomerGroup group) {
        Adult customer = getNullable(group.getCustomer(), adultRepository);
        group.setCustomer(customer);
        Company company = getNullable(group.getCompany(), companyRepository);
        group.setCompany(company);

        int discount = 0;
        if (company != null) {
            discount = company.getDiscount();
        } else if (customer != null) {
            discount = customer.getDiscount();
        }
        return discount;
    }

    private <T extends Identifiable> T getNullable(T nullable, BaseRepository<T> repo) {
        if (nullable != null) {
            if (nullable.getId() != null) {
                nullable = repo.findOne(nullable.getId());
            } else {
                nullable = repo.save(nullable);
            }
        }
        return nullable;
    }

    private void handleCreate(CustomerGroup group) {
        handleIncludeCustomer(group);
        handleGroupMembers(group);
    }

    private void handleGroupMembers(CustomerGroup group) {
        for (GroupMember groupMember : group.getGroupMembers()) {
            Person basePerson = groupMember.getPerson();
            if (basePerson.getId() == null) {
                groupMember.setPerson(personRepository.save(basePerson));
            }
            groupMember.setCustomerGroup(group);
        }
        groupMemberRepository.save(group.getGroupMembers());
    }

    private void handleIncludeCustomer(CustomerGroup group) {
        if (group.isIncludeCustomer()) {
            if (group.getCustomer() == null) {
                throw new CustomerGroupException(CustomerGroupException.INCLUDE_NULL_CUSTOMER, "customer");
            }
            GroupMember customerToInclude = new GroupMember();
            customerToInclude.setPerson(group.getCustomer());
            customerToInclude.setCustomerGroup(group);
            group.getGroupMembers().add(customerToInclude);
        }
    }

    public void updateTotal(CustomerGroup group) {
        repository.updateTotal(group);
    }

    public void updateTotalPaid(CustomerGroup group) {
        repository.updateTotalPaid(group);
    }

    public void setDiscount(CustomerGroup group, int newDiscount, BankDetails details) {
        repository.setDiscount(newDiscount, group);
        group.setDiscount(newDiscount);
        handleDiscountChange(newDiscount, group, details);
    }

    private void handleDiscountChange(int newDiscount, CustomerGroup group, BankDetails details) {
        for (RoomUse roomUse : roomUseRepository.findByCustomerGroupIdAndStatusIn(group.getId(), OPEN_BOOKING_STATUSES)) {
            billService.handleTotalsChange(updateLivingUses(newDiscount, roomUse), details);
        }
    }

    private Iterable<Bill> updateLivingUses(int newDiscount, RoomUse roomUse) {
        List<Bill> billsToUpdate = livingUseRepository.findBillsByRoomUse(roomUse);
        for (Bill bill : billsToUpdate) {
            livingUseService.setDiscount(bill, newDiscount);
        }
        return billsToUpdate;
    }
}