package com.study.demo.domain;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;


public class MailSender {
    private static Logger logger = Logger.getLogger(MailSender.class);

    public static MailAuthenticator authenticator;
    private MimeMessage message;
    private Session session;
    private Transport transport;
    private Properties properties = new Properties();
    private String mailHost;

    /**
     * 初始化邮件发送器
     *
     * @param host        SMTP邮件服务器地址
     * @param username    发送邮件的用户名
     * @param password    发送邮件的密码
     * @param fromName    发件人显示名称
     * @param fromAddress 发送邮件的邮箱地址
     * @param auth        是否需要验证
     * @param port        邮箱发送端口号
     */
    public MailSender(final String host, final String username, final String password,
                      final String fromName, final String fromAddress,
                      final String auth, final String port) {
        init(host, username, password, fromName, fromAddress, auth, port);
    }

    /**
     * 初始化smtp发送邮件所需参数
     */
    private boolean init(String host, String username, String password, String fromName,
                         String fromAddress, String auth, String port) {
        properties.put("mail.smtp.host", host);// mail.envisioncitrix.com
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.port", port);//邮箱发送端口号
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.checkserveridentity", "false");
        properties.put("mail.smtp.ssl.trust", host);
        authenticator = new MailAuthenticator(username, password, fromName, fromAddress);
        session = Session.getInstance(properties, authenticator);
        session.setDebug(false);// 开启后有调试信息
        message = new MimeMessage(session);
        mailHost = host;
        return true;
    }

    /**
     * 暗送邮件
     *
     * @param subject      主题
     * @param bccreceivers 收信人
     * @param textContent  文本内容
     * @param attachments
     * @return
     */
    public boolean sendBcc(String subject, List<String> bccreceivers,
                           String textContent, Map<String, byte[]> attachments) {
        try {
            // 设置发信人
            message.setFrom(new InternetAddress(authenticator.getFromAddress(), authenticator.getFromName()));
            // 收件人(多个)
            InternetAddress[] sendTo = getMailList(bccreceivers);
            message.setRecipients(MimeMessage.RecipientType.BCC, sendTo);
            // 邮件主题
            message.setSubject(subject);
            // 添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(textContent, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 遍历添加附件
            addAttachments(attachments, multipart);
            // 保存邮件
            message.saveChanges();
            // SMTP验证，就是你用来发邮件的邮箱用户名密码
            transport = session.getTransport("smtp");
            transport.connect(mailHost, authenticator.getUsername(), authenticator.getPassword());
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            logger.info(subject + " Email send success!");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error(e);
                }
            }
        }
        return true;
    }

    /**
     * 发送邮件
     *
     * @param subject     主题
     * @param receivers   收信人
     * @param ccreceivers 抄送人
     * @param textContent 文本内容
     * @param attachments 附件
     * @return
     */
    public boolean send(String subject, List<String> receivers, List<String> ccreceivers,
                        String textContent, Map<String, byte[]> attachments) {
        try {
            // 设置发信人
            message.setFrom(new InternetAddress(authenticator.getFromAddress(), authenticator.getFromName()));
            // 收件人(多个)
            InternetAddress[] sendTo = getMailList(receivers);
            message.setRecipients(MimeMessage.RecipientType.TO, sendTo);
            if (ccreceivers != null && ccreceivers.size() > 0) {
                //抄送(多个)
                InternetAddress[] sendCC = getMailList(ccreceivers);
                message.setRecipients(MimeMessage.RecipientType.CC, sendCC);
            }
            // 邮件主题
            message.setSubject(subject);
            // 添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(textContent, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            addAttachments(attachments, multipart);

            // 保存邮件
            message.saveChanges();
            // SMTP验证，就是你用来发邮件的邮箱用户名密码
            transport = session.getTransport("smtp");
            transport.connect(mailHost, authenticator.getUsername(), authenticator.getPassword());
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            logger.info(subject + " Email send success!");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error(e);
                }
            }
        }
        return true;
    }

    /**
     * 添加附件
     *
     * @param attachments
     * @param multipart
     * @throws MessagingException
     */
    private void addAttachments(Map<String, byte[]> attachments, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
        // 遍历添加附件
        if (attachments != null && attachments.size() > 0) {
            for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(entry.getValue(), "application/octet-stream");
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(new String(entry.getKey().getBytes("GBK"), "ISO-8859-1"));
                multipart.addBodyPart(attachmentBodyPart);
            }
        }
        // 将附件放到message中
        message.setContent(multipart);
    }

    /**
     * 发送邮箱列表
     *
     * @param list
     * @return
     * @throws AddressException
     */
    private InternetAddress[] getMailList(List<String> list) throws AddressException {

        final int num = list.size();
        InternetAddress[] addresses = new InternetAddress[num];
        for (int i = 0; i < num; i++) {
            addresses[i] = new InternetAddress(list.get(i));
        }
        return addresses;
    }
}