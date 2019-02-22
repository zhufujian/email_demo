package com.study.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.study.demo.utils.FuncUtils;

public class EmailServiceTest {

    ClassPathXmlApplicationContext context = null;
    IEmailService emailService = null;
    String uuid = "";
    private static String VERSION = "version";//版本号
    private static String TO_ADDRESS = "to_address";//目标邮箱
    private static String CC_ADDRESS = "cc_address";//抄送人列表
    private static String BCC_ADDRESS = "bcc_address";//密送人列表
    private static String SUBJECT = "subject";//邮件主题
    private static String CONTENT = "content";//邮件正文
    private static String ATTACHMENTS = "attachments";//邮件附件
    private static String FROM_USERNAME = "from_username";//发件人用户名
    public static final String CORRELATIONID = "correlationID";

    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext(
                new String[]{"dubbo-consumer.xml"});
        emailService = (IEmailService) context
                .getBean("emailService");
        context.start();
        uuid = FuncUtils.getUuid();
    }

    @Test
    public void sendMailTest() throws Exception {//发送邮件
        JSONObject reqJson = new JSONObject();
        reqJson.put(CORRELATIONID, uuid);
        reqJson.put(VERSION, "1.0");
        reqJson.put(TO_ADDRESS, "1135415342@qq.com");
        //reqJson.put(CC_ADDRESS, "zhu_fujian@163.com");
       // reqJson.put(BCC_ADDRESS, "lihao002@yintong.com.cn,qqqqqqqqq!#@qq.com,zhu_fujian@163.com");
        reqJson.put(SUBJECT, "测试");
        reqJson.put(CONTENT, "这是一封测试邮件！");
        reqJson.put(FROM_USERNAME, "yt_vip");
        System.out.println("发送邮件请求报文[" + reqJson.toJSONString() + "]");
        String resJson = emailService.sendMail(reqJson.toJSONString());
        System.out.println("发送邮件响应报文[" + resJson + "]");
    }

    @Test
    public void sendMailWithAttachmentsTest() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        String[] files = {"D:\\01.png", "D:\\02.png"};
        Map<String, byte[]> attachments = new HashMap<String, byte[]>();
        for (int i = 0; i < files.length; i++) {
            attachments.put(i + "请求.png", fileToBytes(files[i])); 
        }
        params.put(CORRELATIONID, uuid);
        params.put(VERSION, "1.0");
        params.put(TO_ADDRESS, new ArrayList<String>() {{
            add("1135415342@qq.com");
        }});
        params.put(CC_ADDRESS, new ArrayList<String>() {{
            add("1135415342@qq.com");
        }});
        params.put(ATTACHMENTS, attachments);
        params.put(BCC_ADDRESS, new ArrayList<String>() {{
            add("1135415342@qq.com");
        }});
        params.put(SUBJECT, "测试2");
        params.put(CONTENT, "这是一封测试邮件2！");
        params.put(FROM_USERNAME, "yt_vip");
        System.out.println("发送邮件请求报文[" + params.toString() + "]");
        String resJson = emailService.sendMailWithAttachments(params);
        //String resJson = emailService.sendMail(reqJson.toJSONString());
        System.out.println("发送邮件响应报文[" + resJson + "]");
    }

    @After
    public void tearDown() throws Exception {
        context.destroy();
    }

    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmailServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmailServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(EmailServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (null != fis) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(EmailServiceTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return buffer;
    }
}
