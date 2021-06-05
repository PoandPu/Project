package ua.epam.pavelchuk.final_project.web.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

public class SendMail {

	private SendMail() {
	}

	private static final String FROM = "aleksandrspak856@gmail.com";
	private static final Logger LOG = Logger.getLogger(SendMail.class);

	private static Session authentification() {
		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		return Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM, "testForProject");
			}
		});
	}

	public static void sendRefence(String toEmail, String login, String hash) throws AppException {
		// Sender's email ID needs to be mentioned
		Session session = authentification();
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(FROM));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			// Set Subject: header field
			message.setSubject("Restore your password");

			StringBuilder sb = new StringBuilder();
			sb.append("<h2>Hello, ");
			sb.append(login);
			sb.append("!</h2>");
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px \">In order to continue password recovery, you need to follow this link by copying it into the address bar of your browser: ");
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px \"> <form action=\"http://localhost:8080/FinalProjectTesting/controller\" method=\"post\"> <input type=\"hidden\" name=\"command\" value=\"generatePassword\" style = \"background-color:#ededed;border-width:1px;border-style:solid;border-color:#e2e3e7;color:#444444;display:block;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px;padding:10px  \" required>");
			sb.append("<input type=\"hidden\" name=\"hash\" value='");
			sb.append(hash);
			sb.append("' required>");
			sb.append(
					"<input class=\"form\" type=\"submit\" name=\"submit\" value= \"Restore\" style = \"background-color: #4e6e6f;");
			sb.append("  color: white; padding: 12px 40px; border:none;");
			sb.append(" border-radius: 4px; cursor: pointer;\"  />");
			sb.append("</form>");
			sb.append("<br>");
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px \">If this request was initiated by someone else, then you do not need to follow this link!");
			message.setContent(sb.toString(), "text/html; charset=utf-8");

			// Send message
			Transport.send(message);
		} catch (MessagingException mex) {
			LOG.error(Messages.ERR_EMAIL_LOG);
			throw new AppException(Messages.ERR_EMAIL, mex);
		}
	}

	public static void sendNewPassword(String toEmail, String password) throws AppException {
		Session session = authentification();
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(FROM));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			// Set Subject: header field
			message.setSubject("Your new password");

			StringBuilder sb = new StringBuilder();
			sb.append("<h2>Successfully sent</h2>");
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px \">A new password has been successfully generated. We strongly recommend that you change this temporary password to your personal one in your profile settings immediately after successful authorization on the site.");
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:bold;line-height:18px;margin-top:0;margin-bottom:15px \">Your new password: ");
			sb.append(password);
			sb.append(
					"<p style = \"color:#677483;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:18px;margin-top:0;margin-bottom:15px \">When copying the password, make sure that there are no leading and trailing spaces (there should be no spaces at the edges).");
			message.setContent(sb.toString(), "text/html; charset=utf-8");

			// Send message
			Transport.send(message);
		} catch (MessagingException mex) {
			LOG.error(Messages.ERR_EMAIL_LOG);
			throw new AppException(Messages.ERR_EMAIL, mex);
		}
	}
}
