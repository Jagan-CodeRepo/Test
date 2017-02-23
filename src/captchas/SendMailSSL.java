/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package captchas;

/**
 *
 * @author jagan
 */
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendMailSSL 
{
	Properties props;
	Session session;
	public SendMailSSL()
	{
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("contactwebfs","weather123");
				}
			});
	}
	
    public int sendMail(String to,String sub,String txt)
    {
        
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("contactwebfs@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(sub);
			message.setText(txt);
 
			Transport.send(message);
 
			System.out.println("Done");
			
                        return 1;
 
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
                return 0;
        
    }
}
