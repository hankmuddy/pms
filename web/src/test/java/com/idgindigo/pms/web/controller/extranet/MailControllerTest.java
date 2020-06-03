package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.service.extranet.MailService;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.Test;

import static com.idgindigo.pms.web.controller.extranet.MailController.MailDto;

/**
 * @author valentyn_vakatsiienko
 * @since 3/20/14 1:55 PM
 */
public class MailControllerTest extends InMemoryDbWebTest {
    @Tested
    private MailController mockController;
    @Injectable
    private MailService mailService;

    @Test
    public void testSendMail() {
        final String to = "to";
        final String subject = "subject";
        final String body = "body";
        final boolean html = false;
        final MailDto dto = new MailDto(to, subject, body, html);
        mockController.send(dto);

        new Verifications() {{
            MailService.EmailCallback capture = withCapture();
            mailService.sendMail(to, subject, body, html, capture);
        }};
    }

    @Override
    protected String getUrl() {
        return MailController.URL + "/";
    }
}
