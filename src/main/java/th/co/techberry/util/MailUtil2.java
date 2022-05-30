package th.co.techberry.util;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.sql.*;
import javax.mail.internet.MimeMultipart;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.nio.charset.*;
import javax.mail.util.*;
import java.io.*;
import javax.mail.internet.*;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import javax.activation.*;
import java.util.Base64;
import javax.mail.internet.MimeUtility;
import th.co.techberry.constants.ConfigConstants;
import javax.activation.DataSource;
public class MailUtil2 {

//			String from = "bubkim94@gmail.com";
			String from = "newsorazen@gmail.com";
//		    String user = "bubkim94@gmail.com";
			String user = "newsorazen@gmail.com";
//		    String password = "chanasit14";
			String password = "K27e06n99g44";
	public void sendMail(String mailRecipient, String subject, Map<String, Object> dataMap,String template_name)
			throws Exception {
		String host = "smtp.gmail.com";//or IP address  
		String port = "587";
	    Properties properties = System.getProperties();
	    properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        try {
        	Message message = new MimeMessage(session);
    		MimeBodyPart body = createMimeBodyByTemplate(dataMap,template_name);
    		Multipart multipart = new MimeMultipart();
    		multipart.addBodyPart(body);
    		message.setFrom(new InternetAddress(ConfigConstants.MAIL_SMTP_SENDER_USER));
    		/*** Recipient ***/
    		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailRecipient));
    		message.setSubject(subject);
    		message.setContent(multipart, "text/html;charset=UTF-8");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
	}


	private MimeBodyPart createMimeBodyByTemplate(Map<String, Object> dataMap, String template_name) throws Exception {

		Writer out = new StringWriter();
		Configuration cfg = new Configuration();
		FileTemplateLoader templateLoader = new FileTemplateLoader(new File(ConfigConstants.PATH_MAIL_TEMPLATE));
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate(template_name);
		template.process(dataMap,out);
		MimeBodyPart body = new MimeBodyPart();
		body.setContent(out.toString(), "text/html;charset=UTF-8");
		body.setHeader("Content-Transfer-Encoding","quoted-printable");
		return body;
	}

	public void sendMailWIthFile(String mailRecipient, String subject, Map<String, Object> dataMap,
								 String template_name, List<Map<String, Object>> data)
			throws Exception {
		String host = "smtp.gmail.com";//or IP address
		String port = "587";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.user", user);
		properties.setProperty("mail.smtp.password", password);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);

		try {
			javax.mail.Message message = new MimeMessage(session);
			MimeBodyPart body = createMimeBody(dataMap,template_name);
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
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private MimeBodyPart createMimeBody(Map<String, Object> dataMap, String template_name) throws Exception {

		Writer out = new StringWriter();
		Configuration cfg = new Configuration();
		FileTemplateLoader templateLoader = new FileTemplateLoader(new File(ConfigConstants.PATH_MAIL_TEMPLATE));
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate(template_name);
		template.process(dataMap,out);
		MimeBodyPart body = new MimeBodyPart();
		body.setContent(out.toString(), "text/html;charset=UTF-8");
		body.setHeader("Content-Transfer-Encoding","quoted-printable");
		return body;
	}
}
