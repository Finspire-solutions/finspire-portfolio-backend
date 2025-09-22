package com.finspire.email;

import com.finspire.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

//to send acctivation code
    @Async
    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplate,
                          String activationCode,
                          String subject
                          ) throws MessagingException {
        String templateName;
        if (emailTemplate == null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.getName();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String ,Object> properties =new HashMap<>();
        properties.put("username",username);
        properties.put("confirmationUrl","");
        properties.put("activation_code",activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("finspire001@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String template = templateEngine.process(templateName,context);
        helper.setText(template,true);
        mailSender.send(mimeMessage);
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetUrl) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        FileSystemResource logo = new FileSystemResource(new File("src/main/resources/images/logo.png"));
        helper.addInline("logoImage", logo);

        Map<String, Object> properties = new HashMap<>();
        properties.put("customerName", user.getUsername());
        properties.put("resetUrl", resetUrl);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("finspire001@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject("Reset Your Password");
        String template = templateEngine.process("reset_password", context);
        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }

}
