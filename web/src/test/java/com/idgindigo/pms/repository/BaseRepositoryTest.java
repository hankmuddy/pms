package com.idgindigo.pms.repository;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;


public class BaseRepositoryTest extends JpaTests {
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Test
    public void testDeactivate() throws Exception {
        BankDetails bankDetails = bankDetailsProvider.getFirst();
        Assert.assertEquals(bankDetailsProvider.getRepository().findOne(bankDetails.getId()).getActive(), Boolean.TRUE);
        bankDetailsProvider.getRepository().deactivate(bankDetails);
        Assert.assertEquals(bankDetailsProvider.getRepository().findOne(bankDetails.getId()).getActive(), Boolean.FALSE);
    }
}