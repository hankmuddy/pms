package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.service.ServiceTest;
import org.jvnet.mock_javamail.Mailbox;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.idgindigo.pms.utils.EntityProvider.randomString;

/**
 * @author valentyn_vakatsiienko
 * @since 3/20/14 2:04 PM
 */
public class MailServiceTest extends ServiceTest {
    @Inject
    private MailService mailService;

    @Test
    public void testSend() throws Exception {
        final String to = randomString() + "_HappyCustomer@gmail.com";
        String subject = "Hi there";
        final String body = "Fairly big body";

        mailService.sendMail(to, subject, body, false, new MailService.EmailCallback() {
            @Override
            public void onSuccess() {
                String res = null;
                try {
                    res = Mailbox.get(to).get(0).getContent().toString();
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
                Assert.assertEquals(res, body);
            }

            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
                Assert.fail(e.getMessage());
            }
        });
    }

}