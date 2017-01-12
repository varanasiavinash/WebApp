/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment2.model;

/**
 *
 * @author varanasiavinash
 */
import com.assignment2.beans.Mail;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailDB {

     public static boolean sendMessage(Mail mail) {

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "465");
        
        boolean state=true;
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("tothisdonotreply@gmail.com","donotreply143");
                    }
                } );
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress("tothisdonotreply@gmail.com","DoNotReply"));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mail.getReceiverEmail(),mail.getReceiverName()));
            // Set Subject: header field
            message.setSubject(mail.getSubject());
            // Now set the actual message
            
            if(mail.getMailType().equals("contact")){
                message.setText(getContactMsg(mail));
            }
            if(mail.getMailType().equals("recommend")){
                message.setText(getRecommendation(mail));
            }
            if(mail.getMailType().equals("activation")){
                message.setContent(getActivationMsg(mail),"text/html");
            }
             if(mail.getMailType().equals("ResetPassword")){
                message.setContent(getResetPasswordMsg(mail),"text/html");
            }
            // Send message
            Transport.send(message);
            state=true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            state=false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EmailDB.class.getName()).log(Level.SEVERE, null, ex);
            state=false;
        }
        return state;
    }

    private static String getRecommendation(Mail mail) {
    
    return 
    "Hi "+mail.getReceiverName()+ "\n" +mail.getMessage()+ "\n";
	
}

    private static String getContactMsg(Mail mail) {
        
    return 
    "Hi "+mail.getReceiverName()+"\n" +mail.getMessage()+ "\n";
	

    }
    private static Object getActivationMsg(Mail mail) {
        
    return 
    "<html><body><h3><a href="+mail.getMessage()+">Activation Link</a></body></html>";
   
    }
    private static Object getResetPasswordMsg(Mail mail) {
        
    return 
    "<html><body><h3><a href="+mail.getMessage()+">Reset Password Link</a></body></html>";
            
        
	

    }
}
