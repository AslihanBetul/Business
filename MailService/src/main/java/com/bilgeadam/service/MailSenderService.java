package com.bilgeadam.service;

import com.bilgeadam.config.rabbit.model.EmailVerificationModel;
import com.bilgeadam.utilty.JwtTokenManager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

   private final JavaMailSender javaMailSender;
  private final JwtTokenManager jwtTokenManager;
    @RabbitListener(queues = "queueSendVerificationEmail")
   public void sendMailVerifyAccount(EmailVerificationModel model) throws MessagingException {
     String Token= jwtTokenManager.createToken(model.getAuthId()).get();
       String activationLink = "http://localhost:3000/dev/v1/auth/verify-account?token=" + Token;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

       String htmlContent = "<html><body>" +
        "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family:Arial,sans-serif;color:#333;background-color:#f9f9f9;padding:20px;\">" +
        "    <tr>" +
       "        <td style=\"background-color:#ffffff;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);\">" +
        "            <table cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
      "                <tr>" +
     "                    <td style=\"text-align:center;\">" +
       "                        <h1 style=\"color:#333333;font-size:24px;margin:0;\">Hoş Geldiniz, " + model.getFirstName() + "!</h1>" +
       "                        <p style=\"font-size:16px;color:#555555;\">Hesabınızı aktive etmek için aşağıdaki bağlantıya tıklayın:</p>" +
     "                        <a href=\"" + activationLink + "\" style=\"display:inline-block;padding:10px 20px;font-size:16px;color:#ffffff;background-color:#007bff;border-radius:5px;text-decoration:none;\">Aktifleştir</a>" +
       "                    </td>" +
      "                </tr>" +
     "                <tr>" +
       "                    <td style=\"text-align:center;padding-top:20px;\">" +
       "                        <p style=\"font-size:14px;color:#888888;margin:0;\"> Lütfen bu e-postayı yanıtlamayın.</p>" +
      "                    </td>" +
       "                </tr>" +
      "            </table>" +
       "        </td>" +
     "    </tr>" +
       "</table>" +
      "</body></html>";


      helper.setText(htmlContent, true);
       helper.setTo(model.getEmail());
       helper.setSubject("Hoş Geldiniz, " + model.getFirstName() + "!");

      javaMailSender.send(mimeMessage);
   }


}
