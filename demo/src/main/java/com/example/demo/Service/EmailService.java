package com.example.demo.Service;

import com.example.demo.EmailDetails.EmailDetails;
import com.example.demo.Logger.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            CustomLogger.info(
                    "{}: mail to {} sent",
                    this.getClass().getName(), details.getRecipient());
            return true;
        }
        catch (Exception e) {
            CustomLogger.error(
                    "{}: error while sending mail to {}",
                    this.getClass().getName(), details.getRecipient());
            return false;
        }

    }

    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            CustomLogger.info(
                    "{}: mail with attachment to {} sent",
                    this.getClass().getName(), details.getRecipient());
        }

        catch (MessagingException e) {
            CustomLogger.error(
                    "{}: error while sending mail with attachment to {}",
                    this.getClass().getName(), details.getRecipient());
        }
        return null;
    }
}
