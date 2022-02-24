package th.co.techberry.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import th.co.techberry.constants.*;

public class MailUtil {

	public void sendMail(String mailRecipient, String subject, Map<String, Object> dataMap, String template_name)
			throws Exception { 
		Properties props = new Properties();  
		props.put("mail.smtp.host", ConfigConstants.MAIL_SMTP_HOST);
		props.put("mail.smtp.socketFactory.port", ConfigConstants.MAIL_SMTP_PORT);
 		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", ConfigConstants.MAIL_SMTP_PORT);
		Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ConfigConstants.MAIL_SMTP_SENDER_USER,
						ConfigConstants.MAIL_SMTP_SENDER_PASS);
			}
		});

		Message message = new MimeMessage(mailSession);
		MimeBodyPart body = createMimeBodyByTemplate(dataMap,template_name);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(body);
		message.setFrom(new InternetAddress(ConfigConstants.MAIL_SMTP_SENDER_USER));
		/*** Recipient ***/
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailRecipient));
		message.setSubject(subject);
		message.setContent(multipart, "text/html;charset=UTF-8");
		Transport.send(message);
	}
	
	private MimeBodyPart createMimeBodyByTemplate(Map<String, Object> dataMap, String template_name) throws Exception {

		Writer out = new StringWriter();
		Configuration cfg = new Configuration();
		FileTemplateLoader templateLoader = new FileTemplateLoader(new File(ConfigConstants.PATH_MAIL_TEMPLATE));
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate(template_name);
		template.process(dataMap, out);
		MimeBodyPart body = new MimeBodyPart();
		body.setContent(out.toString(), "text/html;charset=UTF-8");
		return body;
	}

}
