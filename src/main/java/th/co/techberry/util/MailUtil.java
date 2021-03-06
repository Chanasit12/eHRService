package th.co.techberry.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

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
		System.out.println("Sent message successfully....");
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

	public void sendMailWIthFile(String mailRecipient, String subject, Map<String, Object> dataMap, String template_name, List<Map<String, Object>> data)
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
		for(Map<String, Object> temp : data){
			String[] Raw_encodedImg = ((String)temp.get("data")).split("base64,");
			String encodedImg = Raw_encodedImg[1];
			String type = (Raw_encodedImg[0].split("/")[0]).split(":")[1]+"/*";
			MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
			filePart.setContent(encodedImg, type);
			filePart.setFileName((String)temp.get("name"));
			multipart.addBodyPart(filePart);
		}
		message.setFrom(new InternetAddress(ConfigConstants.MAIL_SMTP_SENDER_USER));
		/*** Recipient ***/
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailRecipient));
		message.setSubject(subject);
		message.setContent(multipart, "text/html;charset=UTF-8");
		System.out.println("Sent message successfully....");
		Transport.send(message);
	}

	private MimeBodyPart createMimeBody(Map<String, Object> dataMap, String template_name) throws Exception {

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
