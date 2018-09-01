package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.email.exceptions.EmailFactoryException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

public class EmailFactory
{

        private static EmailFactory instance = null;
        final private String smtpHost;
        final private String smtpPort;
        final private String username;
        final private String password;

        private EmailFactory(
                    String smtpHost,
                    String smtpPort,
                    String username,
                    String password
        )
        {
                // TODO: Aggiungere controllo null?

                this.smtpHost = smtpHost;
                this.smtpPort = smtpPort;
                this.username = username;
                this.password = password;
        }

        /**
         * Function to configure EmailFactory. Call this before using the
         * getInstance method
         *
         * @param smtpHost smtp server hostname
         * @param smtpPort smtp server port
         * @param username account username
         * @param password account password
         * @throws EmailFactoryException
         */
        public static void configure(String smtpHost, String smtpPort, String username, String password) throws EmailFactoryException
        {
                if (instance == null)
                {
                        instance = new EmailFactory(smtpHost, smtpPort, username, password);
                }
                else
                {
                        throw new EmailFactoryException("EmailFactory already configured. You can call configure only one time");
                }
        }

        /**
         * Returns the singleton instance
         *
         * @return a EmailFactory singleton
         * @throws EmailFactoryException
         */
        public static EmailFactory getInstance() throws EmailFactoryException
        {
                if (instance == null)
                {
                        throw new EmailFactoryException("EmailFactory not yet configured. Call EmailFactory.configure(String smtpHost, String smtpPort, String username, String password) before use the class");
                }
                return instance;
        }

        /**
         * Send a mail with the specified paramenters(using the account
         * specified during configure)
         *
         * @param personal Name to show(as contact)
         * @param subject subject of the mail
         * @param content the content of the mail
         * @param email_to the mail recipient
         * @throws MessagingException
         * @throws UnsupportedEncodingException
         */
        public void sendMail(
                    String personal,
                    String subject,
                    Multipart content,
                    String email_to
        ) throws MessagingException, UnsupportedEncodingException
        {

                Properties props = System.getProperties();

                props.setProperty("mail.smtp.host", smtpHost);
                props.setProperty("mail.smtp.port", smtpPort);
                props.setProperty("mail.smtp.socketFactory.port", smtpPort);
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.starttls.enable", "true");
                //props.setProperty("mail.debug", "true"); //TODO: Da togliere

                Session session = Session.getInstance(props, new Authenticator()
                {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                                return new PasswordAuthentication(username, password);
                        }
                });

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(username, personal));

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
                msg.setSubject(subject);
                msg.setSentDate(new Date());
                msg.setContent(content);

                Transport.send(msg);
        }

        private String getBasePath(HttpServletRequest request) {
                return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }

        /**
         * metodo per inviare email di registrazione
         *
         * @param user
         * @param request
         * @throws MessagingException
         * @throws UnsupportedEncodingException
         */
        public void sendEmailOfRegistration(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
                ResourceBundle bundle = i18n.getBundle(request);

                String serviceName = "admin";
                String emailSubject = bundle.getString("emailFactory.registerEmail.subject");

                String url = getBasePath(request) + "/service/activateUserService?Email="+ URLEncoder.encode(user.getEmail(), "UTF-8")+"&verifyEmailLink=" +  URLEncoder.encode(user.getVerifyEmailLink(), "UTF-8");


                Multipart content = MessageFacotry.messageOfRegistration(user, url, bundle, request.getServletContext().getInitParameter("siteName"));
                sendMail(serviceName, emailSubject, content, user.getEmail());

        }

        /**
         * metodo per inviare email di reset password
         *
         * @param user
         * @param request
         * @throws MessagingException
         * @throws UnsupportedEncodingException 
         */
        public void sendEmailOfRestPassword(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException
        {
                ResourceBundle bundle = i18n.getBundle(request);

                String serviceName = "admin";
                String emailSubject = bundle.getString("emailFactory.resetPasswordEmail.subject");

                String url = getBasePath(request) + "/resetPassword?Email=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&resetPwdLink=" + URLEncoder.encode(user.getResetPwdEmailLink(), "UTF-8");

                Multipart content = MessageFacotry.messageOfResetPassword(user, url, bundle);
                sendMail(serviceName, emailSubject, content, user.getEmail());
        }

        /**
         * metodo per inviare email di suggerimento di riaquisto
         *
         * @param user
         * @param basepath
         * @param listProduct
         * @throws MessagingException
         * @throws UnsupportedEncodingException
         */
        public void sendEmailOfSuggestionForRepeatitivePurchases(User user, String basepath, List<Product> listProduct) throws MessagingException, UnsupportedEncodingException
        {
                String serviceName = "admin";
                String emailSubject = "Suggerimento per riacquisto";
                Multipart content = MessageFacotry.messageOfSuggestionForRepeatitivePurchases(user, basepath, listProduct);
                sendMail(serviceName, emailSubject, content, user.getEmail());

        }
        
}
