package com.idgindigo.pms.service.merchant;

import org.testng.Assert;
import org.testng.annotations.Test;


public class MerchantServiceUnitTest {
    @Test
    public void testHmac() {
        Assert.assertEquals(MerchantService.hash_hmac("helloworld", "mykey"), "74ae5a4a3d9996d5918defc2c3d475471bbf59ac");
        Assert.assertEquals(MerchantService.hash_hmac("helloworld", "PRIE7$oG2uS-Yf17kEnUEpi5hvW/#AFo"), "c19fccf57c613f1868dd22d586f9571cf6412cd0");
    }

    @Test
    public void testMb_strlen() {
        Assert.assertEquals(MerchantService.mb_strlen("fff", "UTF-8"), 3);
        Assert.assertEquals(MerchantService.mb_strlen("ффф", "UTF-8"), 3);
    }

}