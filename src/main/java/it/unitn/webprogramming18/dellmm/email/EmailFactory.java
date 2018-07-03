package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.email.exceptions.EmailFactoryException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class EmailFactory {
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
    ) {
        // TODO: Aggiungere controllo null?

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    /**
     * Function to configure EmailFactory. Call this before using the getInstance method
     *
     * @param smtpHost smtp server hostname
     * @param smtpPort smtp server port
     * @param username account username
     * @param password account password
     * @throws EmailFactoryException
     */
    public static void configure(String smtpHost, String smtpPort, String username, String password) throws EmailFactoryException {
        if (instance == null) {
            instance = new EmailFactory(smtpHost, smtpPort, username, password);
        } else {
            throw new EmailFactoryException("EmailFactory already configured. You can call configure only one time");
        }
    }

    /**
     * Returns the singleton instance
     *
     * @return a EmailFactory singleton
     * @throws EmailFactoryException
     */
    public static EmailFactory getInstance() throws EmailFactoryException {
        if (instance == null) {
            throw new EmailFactoryException("EmailFactory not yet configured. Call EmailFactory.configure(String smtpHost, String smtpPort, String username, String password) before use the class");
        }
        return instance;
    }

    /**
     * Send a mail with the specified paramenters(using the account specified during configure)
     *
     * @param service_name Name to show(as contact)
     * @param subject      subject of the mail
     * @param content      the content of the mail
     * @param email_to     the mail recipient
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendMail(
            String service_name,
            String subject,
            Multipart content,
            String email_to
    ) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();

        props.setProperty("mail.smtp.host", smtpHost);
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.debug", "true"); //TODO: Da togliere

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username, service_name));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(content);

        Transport.send(msg);
    }
}
