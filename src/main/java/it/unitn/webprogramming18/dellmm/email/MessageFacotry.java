package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MessageFacotry
{

        public static Multipart messageOfRegistration(User user, String path) throws MessagingException, UnsupportedEncodingException
        {
                

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
                            + "                        grazie " + user.getName() + " " + user.getSurname() + " per esserti registrato a XXXXX\n"
                            + "                </h1>\n"
                            + "                <br/>\n"
                            + "                <p>\n"
                            + "                        per convalidare tuo account, click la link seguente\n"
                            + "                </p>\n"
                            + "                <br/>\n"
                            + "                <a href=\"" + path + "/service/activateUserService?Email="+URLEncoder.encode(user.getEmail(), "UTF-8")+"&verifyEmailLink=" +  URLEncoder.encode(user.getVerifyEmailLink(), "UTF-8")+ "\">\n"
                            + "                        convalida\n"
                            + "                </a>\n"
                            + "        </div>\n"
                            + "</div>");

                
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");
                
                Multipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(messageBodyPart);

                return multipart;
        }

        public static Multipart messageOfResetPassword(User user, String path) throws MessagingException, UnsupportedEncodingException
        {
                
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
                            + "                        ciao, " + user.getName() + " " + user.getSurname() + "\n"
                            + "                </h1>\n"
                            + "                <br/>\n"
                            + "                <p>\n"
                            + "                        per reimpostare la tua password, sequi il link seguente \n"
                            + "                </p>\n"
                            + "                <br/>\n"
                            + "                <a href=\"" + path + "/resetPassword?Email="+URLEncoder.encode(user.getEmail(), "UTF-8")+"&resetPwdLink=" + URLEncoder.encode(user.getResetPwdEmailLink(), "UTF-8") + "\">\n"
                            + "                        reset password\n"
                            + "                </a>\n"
                            + "        </div>\n"
                            + "</div>");

                
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");
                
                Multipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(messageBodyPart);

                return multipart;
                
                
        }
}
