package it.unitn.webprogramming18.dellmm.email.messageFactories;

import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class VerifyLinkMail {
    public static Multipart createMessage(User user) throws MessagingException {
        String mail_message =
                "Grazie " + user.getName() + " " + user.getSurname() + " per esserti registrato a " + "Nomexyz\n" +
                        "Clicca su " + user.getVerifyEmailLink() + " per verificare la tua mail.";

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
