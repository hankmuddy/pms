package com.idgindigo.pms.service.extranet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @author valentyn_vakatsiienko
 * @since 3/20/14 11:42 AM
 */
@Service
public class MailService {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static final String EMAIL_SEPARATOR = ";";
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    @Inject
    private Environment environment;
    @Inject
    private JavaMailSender mailSender;

    public void sendMail(final MailWrapper mailToSend, final EmailCallback... callback) {
        new Thread() {
            @Override
            public void run() {
                logger.debug("Sending email to: {}", mailToSend.getTo());
                try {
                    mailSender.send(mailToSend.asMimeMessage(mailSender, false).getMimeMessage());
                    for (EmailCallback emailCallback : callback) {
                        emailCallback.onSuccess();
                    }
                } catch (Exception e) {
                    for (EmailCallback emailCallback : callback) {
                        emailCallback.onFailure(e);
                    }
                }
            }
        }.start();
    }

    public void sendMail(String to, String subject, String content, boolean html, EmailCallback... callback) {
        if ("88".equals(environment.getProperty("mail.environment"))) subject = "88:" + subject;
        else if ("dev".equals(environment.getProperty("mail.environment"))) subject = "DEV:" + subject;
        sendMail(new MailWrapper(environment.getProperty("mail.username"), to, subject, content, html), callback);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MailWrapper {
        {
            System.setProperty("mail.mime.charset", "utf8");
        }

        @NotNull
        private String from;
        @NotNull
        private String to;
        @NotNull
        private String subject;
        @NotNull
        private String mailBody;
        private String cc;
        private String bcc;
        private String replyTo;
        private String contentType;
        private boolean html;

        public MailWrapper(String from, String to, String subject, String content, boolean html) {
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.mailBody = content;
            this.html = html;
        }

        /*
        public SimpleMailMessage asSimpleMailMessage() {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            fillMainFields(simpleMailMessage);
            return simpleMailMessage;
        }
        */
        public MimeMailMessage asMimeMessage(JavaMailSender sender, boolean multipart) throws MessagingException {
            MimeMailMessage message = new MimeMailMessage(sender.createMimeMessage());
            fillMainFields(message);
            MimeMessageHelper helper = new MimeMessageHelper(message.getMimeMessage(), multipart, "UTF-8");
            helper.setText(mailBody, html);
            return message;
        }

        private void fillMainFields(MailMessage message) {
            message.setFrom(from);
            message.setTo(splitMails(to));
            message.setCc(splitMails(cc));
            message.setBcc(splitMails(bcc));
            message.setReplyTo(replyTo == null ? from : replyTo);
            message.setSubject(subject);
            message.setText(mailBody);
        }

        private static String[] splitMails(String mailsString) {
            return mailsString == null ? new String[0] : mailsString.split(EMAIL_SEPARATOR);
        }

        public void setReplyTo(String replyTo) {
            if (pattern.matcher(replyTo).matches()) {
                this.replyTo = replyTo;
            }
        }

        private static String addEmail(String addTo, String email) {
            if (pattern.matcher(email).matches()) {
                return (addTo == null ? "" : addTo) + email + EMAIL_SEPARATOR;
            }
            return addTo;
        }

        public String addTo(String to) {
            return this.to = addEmail(this.to, to);
        }

        public String addCc(String cc) {
            return this.cc = addEmail(this.cc, cc);
        }

        public String addBcc(String bcc) {
            return this.bcc = addEmail(this.bcc, bcc);
        }
    }

    public abstract static class EmailCallback {
        public abstract void onSuccess();

        public void onFailure(Exception e) {
            logger.error("Message sending failed", e);
        }
    }
}
