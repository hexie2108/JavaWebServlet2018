package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MessageFacotry {
    /**
     * Create the multipart to send for an email for registration
     * @param user The registered user
     * @param url The url that the user has to use to validate his email
     * @param bundle bundle from which get the i18n strings to create the email
     * @return The content of the email as Multipart
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static Multipart messageOfRegistration(User user, String url, ResourceBundle bundle) throws MessagingException, UnsupportedEncodingException {
        String thanks = MessageFormat.format(
                bundle.getString("emailFactory.registerEmail.text.thanks"),
                user.getName(), user.getSurname(), ConstantsUtils.SITE_NAME
        );

        String howToValidate = bundle.getString("emailFactory.registerEmail.text.howToValidate");
        String validate = bundle.getString("emailFactory.registerEmail.text.validate");

        StringBuilder htmlMessageBuilder = new StringBuilder();
        htmlMessageBuilder.append(""
            + "<div class=\"body\">"
            + "        <div class=\"main\">"
            + "                <style>\n"
            + "\n"
            + "                        .body{\n"
            + "                                font-family: \"Microsoft Yahei\";\n"
            + "                                background: #FAFAFA;\n"
            + "                                padding: 50px;"
            + "\n"
            + "                        }					\n"
            + "                        .main{\n"
            + "\n"
            + "                                width: 50%;\n"
            + "                                margin: 50px auto;\n"
            + "                                background: white;\n"
            + "                                padding: 50px;\n"
            + "                        }\n"
            + "                        .main h1{\n"
            + "                                margin: 20px 0;\n"
            + "                                 line-height: 50px;"
            + "                        }\n"
            + "                        .main p{\n"
            + "                                margin: 10px 0;\n"
            + "                        }\n"
            + "\n"
            + "                        .main a{\n"
            + "                                display: block;\n"
            + "                                text-align: center;\n"
            + "                                width: 200px;\n"
            + "                                line-height: 40px;\n"
            + "                                color:white;\n"
            + "                                text-decoration: none !important;;\n"
            + "                                background: #17a2b8;\n"
            + "                                transition: .5s;\n"
            + "                                border-radius: 5px;\n"
            + "                        }\n"
            + "                        .main a:hover{\n"
            + "                                background:   #138496;\n"
            + "                        }\n"
            + "                </style>"
            + "                <h1>\n"
        ).append(
                thanks
        ).append(
              "                </h1>\n"
            + "                <br/>\n"
            + "                <p>\n"
        ).append(
                howToValidate
        ).append(
              "                </p>\n"
            + "                <br/>\n"
            + "                <a href=\"").append(url).append("\">\n"
        ).append(
                validate
        ).append(
              "                </a>\n"
            + "        </div>\n"
            + "</div>");


        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart("alternative");
        multipart.addBodyPart(messageBodyPart);

        return multipart;
    }

    /**
     * Create the multipart to send for an email for reset the user password
     * @param user The registered user
     * @param url The url that the user has to use to reset his password
     * @param bundle bundle from which get the i18n strings to create the email
     * @return The content of the email as Multipart
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static Multipart messageOfResetPassword(User user, String url, ResourceBundle bundle) throws MessagingException, UnsupportedEncodingException {
        String hello = MessageFormat.format(
                bundle.getString("emailFactory.resetPasswordEmail.text.hello"),
                user.getName(), user.getSurname()
        );

        String howToResetPassword = bundle.getString("emailFactory.resetPasswordEmail.text.howToResetPassword");
        String resetPassword = bundle.getString("emailFactory.resetPasswordEmail.text.resetPassword");

        StringBuilder htmlMessageBuilder = new StringBuilder();
        htmlMessageBuilder.append(""
            + "<div class=\"body\">"
            + "        <div class=\"main\">"
            + "                <style>\n"
            + "\n"
            + "                        .body{\n"
            + "                                font-family: \"Microsoft Yahei\";\n"
            + "                                background: #FAFAFA;\n"
            + "                                padding: 50px;"
            + "\n"
            + "                        }					\n"
            + "                        .main{\n"
            + "\n"
            + "                                width: 50%;\n"
            + "                                margin: 50px auto;\n"
            + "                                background: white;\n"
            + "                                padding: 50px;\n"
            + "                        }\n"
            + "                        .main h1{\n"
            + "                                margin: 20px 0;\n"
            + "                                 line-height: 50px;"
            + "                        }\n"
            + "                        .main p{\n"
            + "                                margin: 10px 0;\n"
            + "                        }\n"
            + "\n"
            + "                        .main a{\n"
            + "                                display: block;\n"
            + "                                text-align: center;\n"
            + "                                width: 200px;\n"
            + "                                line-height: 40px;\n"
            + "                                color:white;\n"
            + "                                text-decoration: none !important;;\n"
            + "                                background: #17a2b8;\n"
            + "                                transition: .5s;\n"
            + "                                border-radius: 5px;\n"
            + "                        }\n"
            + "                        .main a:hover{\n"
            + "                                background:   #138496;\n"
            + "                        }\n"
            + "                </style>"
            + "                <h1>\n"
        ).append(
                hello
        ).append(
              "                </h1>\n"
            + "                <br/>\n"
            + "                <p>\n"
        ).append(
                howToResetPassword
        ).append(
              "                </p>\n"
            + "                <br/>\n"
            + "                <a href=\"").append(url).append("\">\n"
        ).append(
                resetPassword
        ).append(
              "                </a>\n"
            + "        </div>\n"
            + "</div>");


        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart("alternative");
        multipart.addBodyPart(messageBodyPart);

        return multipart;


    }
}
