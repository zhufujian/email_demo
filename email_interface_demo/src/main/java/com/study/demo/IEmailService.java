package com.study.demo;

import java.util.Map;

public interface IEmailService {

	/**
     * 发送邮件
     *
     * @param reqJSON
     * @return
     * @throws Exception
     */
    public String sendMail(String reqJSON);


    /**
     * 发送带附件邮件
     *
     * @param params
     * @return
     * @throws Exception
     * @see ->EmailServiceTest.sendMailWithAttachmentsTest
     * params参数详情
     * VERSION = "version";//版本号->String
     * TO_ADDRESS = "to_address";//目标邮箱->String
     * CC_ADDRESS = "cc_address";//抄送人列表->List<String>
     * BCC_ADDRESS = "bcc_address";//密送人列表->List<String>
     * SUBJECT = "subject";//邮件主题->String
     * CONTENT = "content";//邮件正文->String
     * ATTACHMENTS = "attachments";//邮件附件-> Map<String, byte[]>
     *    key->  new String("测试邮件.txt".getBytes("GBK"),"ISO-8859-1")
     * FROM_USERNAME = "from_username";//发件人用户名->String(默认)
     */
    public String sendMailWithAttachments(Map<String, Object> params);
}
