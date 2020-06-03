package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

/**
 * @author vomel
 * @since 04.06.14 20:57
 */
public class HotelTest extends BasePersistenceTest<Hotel> {
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testConstraintsSameLcode() {
        String lcode = randomLcode();
        provider.getPersistentEntity(createHotel(lcode));
        provider.getPersistentEntity(createHotel(lcode));
    }

    @Test
    public void testConstraintsNullsAllowed() {
        String lcode = null;
        provider.getPersistentEntity(createHotel(lcode));
        provider.getPersistentEntity(createHotel(lcode));
        provider.getPersistentEntity(createHotel(lcode));
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testConstraintsYarikStyle() {
        String lcode = randomLcode() + " ";//Yarik style
        provider.getPersistentEntity(createHotel(lcode));
    }

    private static Visitor<Hotel> createHotel(final String lcode) {
        return new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setLcode(lcode);
            }
        };
    }

    private static String randomLcode() {
        return RandomStringUtils.random(10, false, true);
    }

    @Inject
    private HotelProvider provider;

    @Override
    protected EntityProvider<Hotel> getProvider() {
        return provider;
    }
}
