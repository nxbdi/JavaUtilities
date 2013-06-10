package com.nxbdi.util.logs;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author spannt
 *
 */
public class EmailChecker {

//
  private static String smtpHostName = "mail.something.com";
  private static boolean debugMode = true;

  /**
	 * @param args
	 */
	public static void main(String[] args) {


			String[] recipients = { "your@email.com" };
			try {
				sendMessage(recipients, "Log Notification",
						"Test", "your@email.com");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
	}



    public boolean isDebugMode() {
        return debugMode;
    }


    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }


    /**
     * Method to send E-mail message using SSL Encryption.
     *
     * @param recipients
     *         Array of recipient E-mail addresses
     * @param subject
     *         Subject line of the E-mail
     * @param message
     *         Message body
     * @param from
     *         E-mail address of the sender
     *
     * @throws MessagingException
     */
    public static void sendMessage(
            String recipients[],
            String subject,
            String message,
            String from) throws MessagingException {
        // Add a Security Provider
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        boolean debug = true;

        // Initialise the config.controller
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHostName);
        props.put("mail.debug", "true");

        // Set authentication
        Session session = Session.getDefaultInstance(props);

        // Set debug mode
        session.setDebug(debug);

        // Set sender and recipients
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/html"); // plain

        // Send message
        Transport.send(msg);
    }
}
