package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.utils.PriceUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.LivingUseProvider;
import com.idgindigo.pms.utils.pms.PeriodRoomTypeInfoProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import mockit.NonStrictExpectations;
import org.joda.time.DateTimeZone;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.CustomerGroup.PurposeOfVisit.BUSINESS;
import static com.idgindigo.pms.domain.extranet.CustomerGroup.PurposeOfVisit.TOURISM;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 5:45 PM
 */
public class TouristTaxServiceTest extends ServiceTest {
    public static final float TOURISM_TAX = 1.0F;
    public static final DateTimeZone TIME_ZONE = DateTimeZone.forID("UTC");

    @Inject
    private TouristTaxService touristTaxService;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private PeriodRoomTypeInfoProvider periodRoomTypeInfoProvider;
    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private SettingsService settingsService;
    @Inject
    private LivingUseProvider livingUseProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private BillProvider billProvider;

    @Test(dataProvider = "testTaxCalculation")
    public void testTaxCalculation(LivingUse livingUse, long expectedAmount) {
        new NonStrictExpectations(settingsService) {{
            settingsService.getTourismTax();
            result = TOURISM_TAX;
        }};
        assertEquals(expectedAmount, touristTaxService.getTaxAmount(livingUse), 1);
    }

    @DataProvider(name = "testTaxCalculation")
    public Object[][] getData_testTaxCalculation() {

        List<Object[]> result = new ArrayList<>(10);

        LivingUse livingUse = livingUseProvider.getPersistentEntity();
        Long livingAmount = livingUse.getLivingAmount();
        long expectedAmount = getExpectedAmount(livingAmount);
        result.add(new Object[]{livingUse, expectedAmount});

        final int discount = 33;
        livingUse = livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(LivingUse entity) {
                entity.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
                    @Override
                    public void visit(Bill entity) {
                        entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                            @Override
                            public void visit(RoomUse entity) {
                                entity.setCustomerGroup(customerGroupProvider.getPersistentEntity(new Visitor<CustomerGroup>() {
                                    @Override
                                    public void visit(CustomerGroup entity) {
                                        entity.setDiscount(discount);
                                    }
                                }));
                            }
                        }));
                    }
                }));
            }
        });
        expectedAmount -= PriceUtils.getDiscountValue(expectedAmount, discount);
        result.add(new Object[]{livingUse, expectedAmount});
        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testTaxCalculationPrecisionLoose")
    public void testTaxCalculationPrecisionLoose(LivingUse livingUse, long expectedAmount) {
        new NonStrictExpectations(settingsService) {{
            settingsService.getTourismTax();
            result = TOURISM_TAX;
        }};
        assertEquals(touristTaxService.getTaxAmount(livingUse), expectedAmount);
    }

    @DataProvider(name = "testTaxCalculationPrecisionLoose")
    public Object[][] getData_testTaxCalculationPrecisionLoose() {
        List<Object[]> result = new ArrayList<>(10);

        addParams(result, 10L, 33, 1L);
        addParams(result, 10L, 77, 1L);
        addParams(result, (long) Integer.MAX_VALUE, 77, 4939213L);

        return result.toArray(new Object[result.size()][]);
    }

    private void addParams(List<Object[]> result, final long defaultPrice, final int disc, long res) {
        LivingUse livingUse = livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(LivingUse entity) {
                entity.setService(livingProvider.getPersistentEntity(new Visitor<Living>() {
                    @Override
                    public void visit(Living entity) {
                        entity.setRoom(virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
                            @Override
                            public void visit(VirtualRoom entity) {
                                entity.setRoomType(roomTypeProvider.getPersistentEntity(new Visitor<RoomType>() {
                                    @Override
                                    public void visit(RoomType entity) {
                                        entity.setDefaultPrice(defaultPrice);
                                    }
                                }));
                            }
                        }));
                    }
                }));
                entity.setLivingAmount(entity.getService().getRoom().roomType().getDefaultPrice());
                entity.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
                    @Override
                    public void visit(Bill entity) {
                        entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                            @Override
                            public void visit(RoomUse entity) {
                                entity.setCustomerGroup(customerGroupProvider.getPersistentEntity(new Visitor<CustomerGroup>() {
                                    @Override
                                    public void visit(CustomerGroup entity) {
                                        entity.setDiscount(disc);
                                    }
                                }));
                            }
                        }));
                    }
                }));
                entity.setTourismTax(TOURISM_TAX);
            }
        });
        result.add(new Object[]{livingUse, res});
    }

    @Test(dataProvider = "testTaxCalculation")
    public void testNegativeTaxValue(LivingUse livingUse, long expectedAmount) {
        new NonStrictExpectations(settingsService) {{
            settingsService.getTourismTax();
            result = -1 * TOURISM_TAX;
        }};
        assertEquals(0, touristTaxService.getTaxAmount(livingUse));
    }

    private static long getExpectedAmount(long price) {
        return (long) Math.ceil(price * TOURISM_TAX / 100.0);
    }

    private static long applyDiscount(long price, int discount) {
        return price - PriceUtils.getDiscountValue(price, discount);
    }

    @Test
    public void testPurposeOfVisitHandling() {
        final CustomerGroup group = customerGroupProvider.getPersistentEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setPov(BUSINESS);
            }
        });
        LivingUse livingUse = livingUseProvider.getTransientEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(LivingUse entity) {
                entity.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
                    @Override
                    public void visit(Bill entity) {
                        entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                            @Override
                            public void visit(RoomUse entity) {
                                entity.setCustomerGroup(group);
                            }
                        }));
                    }
                }));
            }
        });
        new NonStrictExpectations(settingsService) {{
            settingsService.getTourismTax();
            result = TOURISM_TAX;
        }};
        assertEquals(touristTaxService.getTaxAmount(livingUse), 0);
        group.setPov(TOURISM);
        assertNotEquals(touristTaxService.getTaxAmount(livingUse), 0);
    }

    @Test
    public void testTourismTaxFromFullPrice() throws NoSuchMethodException {
        new NonStrictExpectations(settingsService) {{
            settingsService.isTourismTaxFromFullPrice();
            result = true;
        }};

        final LivingUse livingUse = livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(LivingUse entity) {
                entity.setTourismTax(TOURISM_TAX);
            }
        });
        long actual = touristTaxService.getTaxAmount(livingUse);
        long expected = touristTaxService.getTaxAmount(livingUse.getRawTotal(), 0, TOURISM_TAX);
        assertNotEquals(actual, 0);
        assertEquals(actual, expected);

        new NonStrictExpectations(settingsService) {{
            settingsService.isTourismTaxFromFullPrice();
            result = false;
        }};
        actual = touristTaxService.getTaxAmount(livingUse);
        expected = touristTaxService.getTaxAmount(livingUse.getLivingAmount(), 0, TOURISM_TAX);
        assertNotEquals(actual, 0);
        assertEquals(actual, expected);
    }
}
