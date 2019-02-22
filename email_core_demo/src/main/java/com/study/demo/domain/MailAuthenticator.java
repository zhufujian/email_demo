package com.study.demo.domain;

 
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
/**
 * 
*
* @Description: 服务器邮箱登录验证
* @ClassName: MailAuthenticator 
* @author zhufj
* @date 2019年2月1日 上午10:40:35 
*
 */
public class MailAuthenticator extends Authenticator {
 

    /**
     * 发送邮件的用户名
     */
    private String username;
    
    /**
     * 发送邮件的密码
     */
    private String password;
    
	/**
     * 发件人显示名称
     */
    private String fromName;
    
    /**
     * 发送邮件的邮箱地址
     */
    private String fromAddress;
 
    /**
     * 初始化邮箱和密码
     * 
     * @param username	发送邮件的用户名
     * @param password	发送邮件的密码
     * @param fromName	发件人显示名称
     * @param fromAddress	发送邮件的邮箱地址
     */
    public MailAuthenticator(String username, String password, String fromName,String fromAddress) {
    	this.username = username;
    	this.password = password;
    	this.fromName = fromName;
    	this.fromAddress = fromAddress;
    }
 
    String getPassword() {
    	return password;
    }
 
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
    	return new PasswordAuthentication(username, password);
    }
 
    String getUsername() {
    	return username;
    }
 
    public void setPassword(String password) {
    	this.password = password;
    }
 
    public void setUsername(String username) {
    	this.username = username;
    }

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
 
}