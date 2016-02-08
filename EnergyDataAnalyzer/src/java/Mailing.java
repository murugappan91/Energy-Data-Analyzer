/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Murugappan
 * Servlet to generate Mail to recepient mail address for various events.
 */
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailing {
	private static final String PROPERTIES_FILE = "WebContent/WEB-INF/properties/email.properties";
	String d_email = "energydatateam@gmail.com", d_password = "sefall2014",
			d_host = "smtp.gmail.com", d_port = "465", m_to, m_subject, m_text;

	public boolean MailingHTML(String m_to, String m_subject, String m_text) {
		this.m_subject = m_subject;
		this.m_text = m_text;
		this.m_to = m_to;
		boolean status = false;
		String mailContent = m_text;

		Properties props = new Properties();
		props.put("mail.smtp.user", d_email);
		props.put("mail.smtp.host", d_host);
		props.put("mail.smtp.port", d_port);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", d_port);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		SecurityManager security = System.getSecurityManager();

		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			
			MimeMessage msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(mailContent, "text/html");
			
			mp.addBodyPart(htmlPart);
			msg.setContent(mp);
			msg.setSubject(m_subject);
			msg.setFrom(new InternetAddress(d_email));
			msg.setSubject(m_subject);
			msg.setFrom(new InternetAddress(d_email));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(m_to));
			Transport.send(msg);
			status = true;
		} catch (Exception mex) {
			mex.printStackTrace();
			return status;
		}
		return status;
	}
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(d_email, d_password);
		}
	}

}