package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.ChildProvider;
import com.idgindigo.pms.utils.pms.CompanyProvider;
import com.idgindigo.pms.utils.pms.LivingUseProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.domain.pms.RoomUse.Status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 3:47 PM
 */
public class CustomerGroupServiceTest extends ServiceTest {
    public static final int COMPANY_DISCOUNT = 10;
    public static final int CUSTOMER_DISCOUNT = 20;

    @Inject
    private CustomerGroupService service;
    @Inject
    private CustomerGroupProvider provider;
    @Inject
    private AdultProvider adultProvider;
    @Inject
    private ChildProvider childProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private GroupMemberRepository groupMemberRepository;
    @Inject
    private CompanyProvider companyProvider;
    @Inject
    private LivingUseProvider livingUseProvider;
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Test(dataProvider = "testIncludeCustomer")
    public void testIncludeCustomer(CustomerGroup group, boolean include) {
        int expectedSize = group.getGroupMembers().size();
        group = service.create(group);
        assertTrue(groupMemberRepository.findByCustomerGroup(group).size() == (include ? expectedSize + 1 : expectedSize));
    }

    @DataProvider(name = "testIncludeCustomer")
    public Object[][] getData_testIncludeCustomer() {
        List<Object[]> result = new ArrayList<Object[]>(4);
        result.add(getParams(false, false));
        result.add(getParams(true, false));
        result.add(getParams(false, true));
        result.add(getParams(true, true));
        return result.toArray(new Object[result.size()][]);
    }

    private Object[] getParams(boolean include, boolean addMembers) {
        if (addMembers) {
            return new Object[]{getTransientEntity(include, getMembers()), include};
        } else {
            return new Object[]{getTransientEntity(include), include};
        }
    }

    private CustomerGroup getTransientEntity(boolean include, List<GroupMember> members) {
        if (members == null) {
            members = Collections.emptyList();
        }
        CustomerGroup group = getTransientEntity(include);
        group.getGroupMembers().addAll(members);
        return group;
    }

    private CustomerGroup getTransientEntity(final boolean include) {
        return provider.getTransientEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setIncludeCustomer(include);
            }
        });
    }

    private List<GroupMember> getMembers() {
        List<GroupMember> members = new ArrayList<>();
        Adult trPerson = adultProvider.getTransientEntity();
        members.add(new GroupMember(trPerson, null));
        Adult perPerson = adultProvider.getPersistentEntity();
        members.add(new GroupMember(perPerson, null));
        Child trChild = childProvider.getTransientEntity();
        members.add(new GroupMember(trChild, null));
        Child perChild = childProvider.getPersistentEntity();
        members.add(new GroupMember(perChild, null));
        return members;
    }

    @Test(dataProvider = "testGetDiscount")
    public void testGetDiscount(CustomerGroup group, int expectedDiscount) {
        group = service.create(group);
        assertEquals(group.getDiscount(), expectedDiscount);
    }

    @DataProvider(name = "testGetDiscount")
    public Object[][] getData_testGetDiscount() {
        List<Object[]> result = new ArrayList<>(3);

        result.add(new Object[]{getGroup(false, false), COMPANY_DISCOUNT});
        result.add(new Object[]{getGroup(true, false), CUSTOMER_DISCOUNT});
        result.add(new Object[]{getGroup(false, true), COMPANY_DISCOUNT});

        return result.toArray(new Object[result.size()][]);
    }

    public CustomerGroup getGroup(final boolean isCompanyNull, final boolean isCustomerNull) {
        return provider.getTransientEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setCompany(isCompanyNull ? null : companyProvider.getPersistentEntity(new Visitor<Company>() {
                    @Override
                    public void visit(Company entity) {
                        entity.setDiscount(COMPANY_DISCOUNT);
                    }
                }));
                entity.setCustomer(isCustomerNull ? null : adultProvider.getPersistentEntity(new Visitor<Adult>() {
                    @Override
                    public void visit(Adult entity) {
                        entity.setDiscount(CUSTOMER_DISCOUNT);
                    }
                }));
            }
        });
    }

    @Test(dataProvider = "testCascade")
    public void testCascade(Visitor<CustomerGroup> cascadeVisitor, BaseIdentifiable nonCascadeEntity, Visitor<CustomerGroup> nonCascadeVisitor, GetterInvoker<CustomerGroup, ? extends BaseIdentifiable> invoker) {
        CustomerGroup group = provider.getTransientEntity(cascadeVisitor);
        group = service.create(group);
        assertNotNull(invoker.invoke(group).getId());

        group = provider.getTransientEntity(nonCascadeVisitor);
        group = service.create(group);
        assertEquals(nonCascadeEntity, invoker.invoke(group));
    }

    @Test
    public void testReuse() {
        final Adult customer = adultProvider.getPersistentEntity(new Visitor<Adult>() {
            @Override
            public void visit(Adult entity) {
                do {
                    entity.setEmail(EntityProvider.randomAlphabeticString());
                } while (((AdultRepository) adultProvider.getRepository()).findByEmail(entity.getEmail()) != null);
            }
        });
        final Adult duplicate = adultProvider.getTransientEntity(new Visitor<Adult>() {
            @Override
            public void visit(Adult entity) {
                entity.setEmail(customer.getEmail());
            }
        });
        CustomerGroup group = provider.getTransientEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setCustomer(duplicate);
            }
        });
        CustomerGroup created = service.create(group);
        assertEquals(created.getCustomer(), customer);
    }

    @DataProvider(name = "testCascade")
    public Object[][] getData_testCascade() {
        List<Object[]> result = new ArrayList<>(2);

        final Adult customer = adultProvider.getPersistentEntity();
        result.add(new Object[]{
                new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setCustomer(adultProvider.getTransientEntity());
                    }
                },
                customer,
                new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setCustomer(customer);
                    }
                },
                new GetterInvoker<CustomerGroup, Adult>() {
                    @Override
                    public Adult invoke(CustomerGroup group) {
                        return group.getCustomer();
                    }
                }
        });

        final Company company = companyProvider.getPersistentEntity();
        result.add(new Object[]{
                new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setCompany(companyProvider.getTransientEntity());
                    }
                },
                company,
                new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setCompany(company);
                    }
                },
                new GetterInvoker<CustomerGroup, Company>() {
                    @Override
                    public Company invoke(CustomerGroup group) {
                        return group.getCompany();
                    }
                }
        });

        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testSetDiscount")
    public void testSetDiscount(final int oldDiscount, int newDiscount, final long rawTotal, final long oldTotal, long newTotal) {
        final CustomerGroup group = provider.getPersistentEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setDiscount(oldDiscount);
            }
        });

        List<RoomUse> roomUses = new ArrayList<>(Status.values().length);
        for (final Status status : Status.values()) {
            roomUses.add(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                @Override
                public void visit(RoomUse entity) {
                    entity.setStatus(status);
                    entity.setCustomerGroup(group);
                }
            }));
        }

        List<LivingUse> livingUses = new ArrayList<>(roomUses.size());
        for (final RoomUse roomUse : roomUses) {
            livingUses.add(livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
                @Override
                public void visit(LivingUse entity) {
                    entity.setRoomUse(roomUse);
                    entity.setRawTotal(rawTotal);
                    entity.setTotal(oldTotal);
                }
            }));
        }

        service.setDiscount(group, newDiscount, bankDetailsProvider.getPersistentEntity());

        for (LivingUse livingUse : livingUses) {
            LivingUse updated = livingUseProvider.getRepository().findOne(livingUse.getId());
            if (CustomerGroupService.OPEN_BOOKING_STATUSES.contains(updated.getRoomUse().getStatus())) {
                assertEquals(updated.getTotal().longValue(), newTotal);
            } else {
                assertEquals(updated.getTotal().longValue(), oldTotal);
            }
        }
    }

    @DataProvider(name = "testSetDiscount")
    public Object[][] getData_testSetDiscount() {
        long total = 100;
        List<Object[]> result = new ArrayList<>(1);
        result.add(new Object[]{0, 100, total, total, 0});
        result.add(new Object[]{100, 0, total, 0, total});
        result.add(new Object[]{50, 50, total * 2, total, total});
        result.add(new Object[]{25, 75, total, 75, 25});
        result.add(new Object[]{75, 25, total, 25, 75});
        return result.toArray(new Object[result.size()][]);
    }

    public static abstract class GetterInvoker<E extends BaseIdentifiable, T extends BaseIdentifiable> {
        public abstract T invoke(E e);
    }
}