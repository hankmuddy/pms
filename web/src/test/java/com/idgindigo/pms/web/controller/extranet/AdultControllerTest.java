package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import com.idgindigo.pms.web.controller.DiscountDto;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 12:53 PM
 */
public class AdultControllerTest extends BaseWebCrudTest<Adult> {
    @Inject
    private AdultProvider provider;

    @Test
    public void testSetDiscount() throws Exception {
        Adult adult = provider.getPersistentEntity(new Visitor<Adult>() {
            @Override
            public void visit(Adult entity) {
                entity.setDiscount(50);
            }
        });
        DiscountDto discountDto = new DiscountDto(100);
        mvc.perform(preparePut(adult.getId() + "/discount").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(discountDto))).andExpect(status().isOk());
        adult = provider.getRepository().findOne(adult.getId());
        assertEquals(adult.getDiscount(), 100);
    }

    @Override
    protected EntityProvider<Adult> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return AdultController.URL + "/";
    }
}
