package it.unitn.webprogramming18.dellmm.email.messageFactories;

import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class VerifyLinkMail {
    public static Multipart createMessage(User user, ResourceBundle languageBundle) throws MessagingException {
        
        //I18nEmail-----------------------------------------
        String[] email_text = {languageBundle.getString("emailText.label.registerEmailText1"),
                               languageBundle.getString("emailText.label.registerEmailText2"),
                               languageBundle.getString("emailText.label.registerEmailText3")};
        
        String mail_message = MessageFormat.format("{0} " +user.getName()+" "+user.getSurname()+ " {1} " +user.getVerifyEmailLink()+ " {2}" , (Object[]) email_text);
        
        //--------------------------------------------------
        
        //String mail_message =
        //    "Grazie "+user.getName()+" "+user.getSurname()+" per esserti registrato a "+"Nomexyz\n"+
        //    "Clicca su "+user.getVerifyEmailLink()+" per verificare la tua mail.";

        StringBuilder htmlMessageBuilder = new StringBuilder();
        mail_message = mail_message.replace(" ", "&nbsp;");
        mail_message = mail_message.replace("\n", "<br>");
        htmlMessageBuilder.append(mail_message).append("<br>");

        Multipart multipart = new MimeMultipart("alternative");

        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(mail_message + "\n");

        BodyPart messageBodyPart2 = new MimeBodyPart();
        messageBodyPart2.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");

        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        return multipart;
    }
}
