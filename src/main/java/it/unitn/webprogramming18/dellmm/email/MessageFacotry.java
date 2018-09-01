package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MessageFacotry
{

        /**
         * Create the multipart to send for an email for registration
         *
         * @param user The registered user
         * @param url The url that the user has to use to validate his email
         * @param bundle bundle from which get the i18n strings to create the
         *               email
         * @param siteName the name of the site
         * @return The content of the email as Multipart
         * @throws MessagingException
         * @throws UnsupportedEncodingException
         */
        public static Multipart messageOfRegistration(User user, String url, ResourceBundle bundle, String siteName) throws MessagingException, UnsupportedEncodingException
        {
                String thanks = MessageFormat.format(
                            bundle.getString("emailFactory.registerEmail.text.thanks"),
                            user.getName(), user.getSurname(), siteName
                );

                String howToValidate = bundle.getString("emailFactory.registerEmail.text.howToValidate");
                String validate = bundle.getString("emailFactory.registerEmail.text.validate");

                StringBuilder htmlMessageBuilder = new StringBuilder();
                htmlMessageBuilder.append(""
                            + "<div class=\"body\" style=\"font-family: \"Microsoft Yahei\"; background: #FAFAFA; padding: 20px;\">"
                            + "        <div class=\"main\" style=\"width: 80%; margin: 50px auto;   background: white;   padding: 20px; \">"
                            + "                <h1 style=\" margin: 30px 0;   line-height: 50px;\">\n"
                ).append(
                            thanks
                ).append(
                            "                </h1>\n"
                            + "                <p style=\"margin: 20px 0;\">\n"
                ).append(
                            howToValidate
                ).append(
                            "                </p>\n"
                            + "                <a style=\"display: block;  text-align: center; width: 200px; line-height: 40px;  color:white; text-decoration: none !important;  background: #17a2b8;   border-radius: 5px; \" href=\"").append(url).append("\">\n"
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
         *
         * @param user The registered user
         * @param url The url that the user has to use to reset his password
         * @param bundle bundle from which get the i18n strings to create the
         * email
         * @return The content of the email as Multipart
         * @throws MessagingException
         * @throws UnsupportedEncodingException
         */
        public static Multipart messageOfResetPassword(User user, String url, ResourceBundle bundle) throws MessagingException, UnsupportedEncodingException
        {
                String hello = MessageFormat.format(
                            bundle.getString("emailFactory.resetPasswordEmail.text.hello"),
                            user.getName(), user.getSurname()
                );

                String howToResetPassword = bundle.getString("emailFactory.resetPasswordEmail.text.howToResetPassword");
                String resetPassword = bundle.getString("emailFactory.resetPasswordEmail.text.resetPassword");

                StringBuilder htmlMessageBuilder = new StringBuilder();
                htmlMessageBuilder.append(""
                            + "<div class=\"body\" style=\"font-family: \"Microsoft Yahei\"; background: #FAFAFA; padding: 20px;\">"
                            + "        <div class=\"main\" style=\"width: 80%; margin: 50px auto;   background: white;   padding: 20px; \">"
                            + "                <h1 style=\" margin: 30px 0;   line-height: 50px;\">\n"
                ).append(
                            hello
                ).append(
                            "                </h1>\n"
                            + "                <p style=\"margin: 20px 0;\">\n"
                ).append(
                            howToResetPassword
                ).append(
                            "                </p>\n"
                            + "                <a style=\"display: block;  text-align: center; width: 200px; line-height: 40px;  color:white; text-decoration: none !important;  background: #17a2b8;   border-radius: 5px; \" href=\"").append(url).append("\">\n"
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

        public static Multipart messageOfSuggestionForRepeatitivePurchases(User user, String path, List<Product> listProduct) throws MessagingException, UnsupportedEncodingException
        {
                Locale locale = Locale.forLanguageTag("it");
                ResourceBundle bundle = i18n.getBundle(locale);

                String hello = MessageFormat.format(
                            bundle.getString("emailFactory.suggestionForRepeatitivePurchases.text.hello"),
                            user.getName(), user.getSurname()
                );
                String messageSuggestion = bundle.getString("emailFactory.suggestionForRepeatitivePurchases.text.message");

                StringBuilder htmlMessageBuilder = new StringBuilder();
                htmlMessageBuilder.append(""
                            + "<div class=\"body\" style=\"font-family: \"Microsoft Yahei\"; background: #FAFAFA; padding: 20px;\">"
                            + "        <div class=\"main\" style=\"width: 80%; margin: 50px auto;   background: white;   padding: 20px; \">"
                            + "                <h1 style=\" margin: 30px 0;   line-height: 50px;\">\n"
                ).append(
                            hello
                ).append(
                            "                </h1>\n"
                            + "                <p style=\"margin: 20px 0;\">\n"
                ).append(
                            messageSuggestion
                ).append(
                            "                </p>\n"
                            + "                 <div class=\"list-product\">"
                );

                for (Product product : listProduct)
                {
                        htmlMessageBuilder.append(""
                                    + "<div class=\"list-item\" style=\" width: 42%; padding: 1%; margin: 1%;  display: inline-block;  border-radius: .25rem;  border: 1px solid #d0d0d0;  \">"
                                    + "         <a style=\" color: #17a2b8; text-decoration: none !important; \" href=\"" + path + "/search?searchWords=" + URLEncoder.encode(product.getName(), "UTF-8") + " \">\n"
                                    + "                  <img  style=\" max-width: 100%;  height: auto; \" src=\"" + path + "/image/product/" + product.getImg() + "\" alt=\"" + product.getName() + "\" />      \n"
                                    + "                 <p style=\" text-align: center;  font-weight: bold; \">" + product.getName() + "</p>"
                                    + "          </a>\n"
                                    + "</div>\n");
                }

                htmlMessageBuilder.append("</div>"
                            + "         </div>\n"
                            + "</div>");

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");

                Multipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(messageBodyPart);

                return multipart;
        }
}
