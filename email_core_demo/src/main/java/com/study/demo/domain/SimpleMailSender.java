package com.study.demo.domain;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 简单邮件发送器，可单发，群发。
 * @author zhufj
 * @2015-5-29 下午1:37:36
 */
public class SimpleMailSender {
 
    /**
     * 发送邮件的props文件
     */
    private final transient Properties props = System.getProperties();
    /**
     * 邮件服务器登录验证
     */
    private transient MailAuthenticator authenticator;
 
    /**
     * 邮箱session
     */
    private transient Session session;
 
    /**
     * 初始化邮件发送器
     * @param host	SMTP邮件服务器地址
     * @param username	发送邮件的用户名
     * @param password	发送邮件的密码
     * @param fromName	发件人显示名称
     * @param fromAddress	发送邮件的邮箱地址
     * @param auth	是否需要验证
     * @param port  邮箱发送端口号
     */
    public SimpleMailSender(final String host, final String username,
        final String password, final String fromName,final String fromAddress , final String auth , final String port) {
    	init(host, username,password,fromName,fromAddress,auth,port);
    }
 
 
    /**
     * 初始化
     * 
     * @param host	SMTP邮件服务器地址
     * @param username	发送邮件的用户名
     * @param password	发送邮件的密码
     * @param fromName	发件人显示名称
     * @param fromAddress	发送邮件的邮箱地址
     * @param auth	是否需要验证
     * @param port  邮箱发送端口号
     */
    private void init(String host, String username, String password, String fromName,String fromAddress, String auth, String port) {

	    props.put("mail.smtp.auth", auth); //验证、或不验证
	    props.put("mail.smtp.host", host);//邮件发送地址
	    props.put("mail.smtp.port", port);//邮箱发送端口号
	    // 验证
	    authenticator = new MailAuthenticator(username, password, fromName,fromAddress);
	    // 创建session
	    session = Session.getInstance(props, authenticator);
    
    }
 
    /**
     * 发送邮件
     * 
     * @param recipient	收件人邮箱地址
     * @param subject	邮件主题
     * @param content	邮件内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException 
     */
    public void send(String recipient, String subject, Object content)
        throws AddressException, MessagingException, UnsupportedEncodingException {
	    // 创建mime类型邮件
	    final MimeMessage message = new MimeMessage(session);
	    // 设置发信人
	    message.setFrom(new InternetAddress(authenticator.getFromAddress(),authenticator.getFromName()));
	    // 设置收件人
	    message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
	    // 设置主题
	    message.setSubject(subject);
	    // 设置邮件内容
	    message.setContent(content.toString(), "text/html;charset=utf-8");
	    // 发送
	    Transport.send(message);
    }
 
    /**
     * 群发邮件
     * @param recipients  主送收件人列表
     * @param mailCcList	抄送收件人列表
     * @param subject	主题
     * @param content	内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(List<String> recipients,List<String> mailCcList, String subject, Object content)
        throws AddressException, MessagingException, UnsupportedEncodingException {
	    // 创建mime类型邮件
	    final MimeMessage message = new MimeMessage(session);
	    // 设置发信人
	    message.setFrom(new InternetAddress(authenticator.getFromAddress(),authenticator.getFromName()));
	    // 设置收件人们
	    InternetAddress[] addresses= getMailList(recipients);
	    message.setRecipients(RecipientType.TO, addresses);
	    if(mailCcList!=null){
	    	 InternetAddress[] mailCcAddresses= getMailList(mailCcList);
	    	 message.setRecipients(RecipientType.CC, mailCcAddresses); // 抄送人 
	    }
	    // 设置主题
	    message.setSubject(subject);
	    // 设置邮件内容
	    message.setContent(content.toString(), "text/html;charset=utf-8");
	    // 发送
	    Transport.send(message);
    }

    /**
     * 群发邮件
     * @param mailBccList	暗送收件人列表
     * @param subject	主题
     * @param content	内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendBcc(List<String> mailBccList, String subject, Object content)
        throws AddressException, MessagingException, UnsupportedEncodingException {
	    // 创建mime类型邮件
	    final MimeMessage message = new MimeMessage(session);
	    // 设置发信人
	    message.setFrom(new InternetAddress(authenticator.getFromAddress(),authenticator.getFromName()));
	    // 设置暗送收件人们
		InternetAddress[] mailBccAddresses = getMailList(mailBccList);
		message.setRecipients(RecipientType.BCC, mailBccAddresses);
	    // 设置主题
	    message.setSubject(subject);
	    // 设置邮件内容
	    message.setContent(content.toString(), "text/html;charset=utf-8");
	    // 发送
	    Transport.send(message);
    }
    /**
     * 发送邮箱列表
     * @param list
     * @return
     * @throws AddressException
     */
 private InternetAddress[]  getMailList(List<String> list) throws AddressException{
	 
	 final int num = list.size();
	    InternetAddress[] addresses = new InternetAddress[num];
	    for (int i = 0; i < num; i++) {
				addresses[i] = new InternetAddress(list.get(i));
	    }
	    return addresses;
 }
 
 
}