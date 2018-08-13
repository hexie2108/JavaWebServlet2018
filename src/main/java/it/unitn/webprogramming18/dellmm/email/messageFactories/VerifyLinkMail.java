package it.unitn.webprogramming18.dellmm.email.messageFactories;

import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class VerifyLinkMail
{

        public static Multipart createMessage(User user, String path) throws MessagingException
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
                            + "                        grazie " + user.getName()+" "+user.getSurname() +" per esserti registrato a XXXXX\n"
                            + "                </h1>\n"
                            + "                <br/>\n"
                            + "                <p>\n"
                            + "                        per convalidare tuo account, click la link seguente\n"
                            + "                </p>\n"
                            + "                <br/>\n"
                            + "                <a href=\""+path+"/service/validateUserService?verifyEmailLink="+user.getVerifyEmailLink()+"\">\n"
                            + "                        convalida\n"
                            + "                </a>\n"
                            + "        </div>\n"
                            + "</div>");
            

                Multipart multipart = new MimeMultipart("alternative");
  
                BodyPart messageBodyPart2 = new MimeBodyPart();
                messageBodyPart2.setContent(htmlMessageBuilder.toString(), "text/html; charset=utf-8");

                multipart.addBodyPart(messageBodyPart2);

                return multipart;
        }
}
