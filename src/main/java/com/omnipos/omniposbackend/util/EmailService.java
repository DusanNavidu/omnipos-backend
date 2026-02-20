package com.omnipos.omniposbackend.util;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Dusan
 * @date 2/19/2026
 */

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendHtmlLoginAlert(String toEmail, String subject, String userName, String userRole, String messageContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlBody =
                    "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden;'>" +
                            "<div style='background-color: #1a73e8; color: white; padding: 20px; text-align: center;'>" +
                            "<h1 style='margin: 0; font-size: 24px;'>OmniPOS Security</h1>" +
                            "</div>" +
                            "<div style='padding: 30px; color: #333; line-height: 1.6;'>" +
                            "<h2 style='color: #1a73e8;'>Login Notification</h2>" +
                            "<p>Hello <strong>" + userName + "</strong>,</p>" +
                            "<p>" + messageContent + "</p>" +
                            "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-top: 20px;'>" +
                            "<p style='margin: 5px 0;'><strong>User Role:</strong> " + userRole + "</p>" +
                            "<p style='margin: 5px 0;'><strong>Status:</strong> Successful Login</p>" +
                            "</div>" +
                            "<p style='margin-top: 30px;'>If this was not you, please secure your account immediately.</p>" +
                            "</div>" +
                            "<div style='background-color: #f1f1f1; color: #777; padding: 15px; text-align: center; font-size: 12px;'>" +
                            "&copy; 2026 OmniPOS System. All rights reserved." +
                            "</div>" +
                            "</div>";

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true කියන්නේ HTML බව අඟවන්න
            helper.setFrom("omnipos.system@gmail.com");

            mailSender.send(message);
            System.out.println("HTML Email sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("HTML Email failed: " + e.getMessage());
        }
    }
}