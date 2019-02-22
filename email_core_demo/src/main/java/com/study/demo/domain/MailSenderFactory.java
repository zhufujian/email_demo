package com.study.demo.domain;

import org.apache.log4j.Logger;

import com.study.demo.utils.SpringConfigUtil;


/**
 * 
*
* @Description: 发件箱工厂
* @ClassName: MailSenderFactory 
* @author zhufj
* @date 2019年2月1日 上午10:41:03 
*
 */
public class MailSenderFactory {
    //带附件服务邮箱
    private static MailSender mailSender = null;
    //暗送带附件服务邮箱
    private static MailSender mailSenderVip = null;
    //服务邮箱
    private static SimpleMailSender serviceSms = null;
    //暗送服务邮箱
    private static SimpleMailSender serviceSmsVip = null;
    private static final String host = SpringConfigUtil.getProperties("mail.host");  //smtp地址
    private static final String username = SpringConfigUtil.getProperties("mail.username");  //邮箱用户名
    private static final String password = SpringConfigUtil.getProperties("mail.password"); //邮箱密码
    private static final String fromName = SpringConfigUtil.getProperties("mail.fromName"); //发件人显示名字
    private static final String fromAddress = SpringConfigUtil.getProperties("mail.fromAddress");  //邮箱地址
    private static final String auth = SpringConfigUtil.getProperties("mail.auth"); //是否需要验证
    private static final String port = SpringConfigUtil.getProperties("mail.port"); //邮箱发送端口号
    //暗送邮箱
    private static final String usernameVip = SpringConfigUtil.getProperties("mail.usernameVip"); //vip发送方邮箱用户名
    private static final String passwordVip = SpringConfigUtil.getProperties("mail.passwordVip"); //vip发送方邮箱密码
    private static final String fromAddressVip = SpringConfigUtil.getProperties("mail.fromAddressVip"); //vip发送方邮箱地址

    /**
     * 获取邮箱
     *
     * @param type 邮箱类型
     * @return 符合类型的邮箱
     */
    public static SimpleMailSender getServiceSender() {
        if (serviceSms == null) {
            serviceSms = new SimpleMailSender(host, username, password, fromName, fromAddress, auth, port);
        }
        return serviceSms;
    }

    /**
     * 获取暗送邮箱
     *
     * @param type 邮箱类型
     * @return 符合类型的邮箱
     */
    public static SimpleMailSender getVipSender() {
        if (serviceSmsVip == null) {
            serviceSmsVip = new SimpleMailSender(host, usernameVip, passwordVip, fromName, fromAddressVip, auth, port);
        }
        return serviceSmsVip;
    }

    /**
     * @param from_username
     * @param log
     * @param tag
     * @return
     * @Description 区分发送邮箱
     * @Return SimpleMailSender
     */
    public static SimpleMailSender getSender(String from_username, Logger log, String tag) {
        if (usernameVip.equals(from_username)) {
            log.info(tag + "获取发送邮箱[" + usernameVip + "]服务");
            return getVipSender();
        }
        log.info(tag + "获取发送邮箱[" + username + "]服务");
        return getServiceSender();
    }

    /**
     * 获取带附件服务邮箱
     *
     * @return
     */
    public static MailSender getServiceSenderExt() {
        if (mailSender == null) {
            mailSender = new MailSender(host, username, password, fromName, fromAddress, auth, port);
        }
        return mailSender;
    }

    /**
     * 获取暗送带附件邮箱
     *
     * @return 符合类型的邮箱
     */
    public static MailSender getVipSenderExt() {
        if (mailSenderVip == null) {
            mailSenderVip = new MailSender(host, usernameVip, passwordVip, fromName, fromAddressVip, auth, port);
        }
        return mailSenderVip;
    }

    /**
     * @param from_username
     * @param log
     * @param tag
     * @return
     * @Description 区分发送带附件邮箱
     * @Return MailSender
     */
    public static MailSender getSenderExt(String from_username, Logger log, String tag) {
        if (usernameVip.equals(from_username)) {
            log.info(tag + "获取发送邮箱[" + usernameVip + "]服务");
            return getVipSenderExt();
        }
        log.info(tag + "获取发送邮箱[" + username + "]服务");
        return getServiceSenderExt();
    }


}