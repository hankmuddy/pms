package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.restutils.exception.BankDetailsException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 1:09 PM
 */
public class BankDetailsControllerTest extends BaseWebCrudTest<BankDetails> {
    @Inject
    private BankDetailsProvider provider;
    @Inject
    private BankDetailsRepository repository;

    /*@Test
    public void testFirstDetailsBecomeDefault() throws Exception {
        new NonStrictExpectations(repository){{
            repository.count();
            result = 0;
        }};
        BankDetails details = provider.getTransientEntity();
        details = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(details)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), BankDetails.class);
        assertTrue(repository.findOne(details.getId()).getDefaultDetails());
    }*/

    @Test
    public void testUniqueDefaultDetails() throws Exception {
        BankDetails one = provider.getPersistentEntity();
        BankDetails two = provider.getPersistentEntity();
        assertNotEquals(repository.getDefault(), one);
        assertNotEquals(repository.getDefault(), two);

        setDefault(one);
        assertEquals(repository.getDefault(), one);
        assertNotEquals(repository.getDefault(), two);

        setDefault(two);
        assertNotEquals(repository.getDefault(), one);
        assertEquals(repository.getDefault(), two);
    }

    @Test
    public void testBlock() throws Exception {
        BankDetails details = provider.getPersistentEntity();
        assertFalse(details.getBlocked());

        setBlocked(details, true);
        details = repository.findOne(details.getId());
        assertTrue(details.getBlocked());

        setBlocked(details, false);
        details = repository.findOne(details.getId());
        assertFalse(details.getBlocked());
    }

    @Test
    public void testBlockDefaultDetails() throws Exception {
        BankDetails details = provider.getPersistentEntity();
        setDefault(details);

        BankDetailsController.BlockedDto dto = new BankDetailsController.BlockedDto();
        dto.setBlocked(true);

        testBadRequest(getBlockedRequest(details, dto), BankDetailsException.BLOCK_DEFAULT_DETAILS);
    }

    private void setBlocked(BankDetails details, boolean blocked) throws Exception {
        BankDetailsController.BlockedDto dto = new BankDetailsController.BlockedDto();
        dto.setBlocked(blocked);
        mvc.perform(getBlockedRequest(details, dto)).andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder getBlockedRequest(BankDetails details, BankDetailsController.BlockedDto dto) throws Exception {
        return preparePut(dto, details.getId() + "/blocked");
    }

    private void setDefault(BankDetails details) throws Exception {
        mvc.perform(preparePut(details.getId() + "/default")).andExpect(status().isOk());
    }

    @Override
    protected EntityProvider<BankDetails> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return BankDetailsController.URL + "/";
    }
}
