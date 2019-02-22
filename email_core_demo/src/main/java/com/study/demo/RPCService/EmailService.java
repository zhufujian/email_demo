package com.study.demo.RPCService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.demo.IEmailService;
import com.study.demo.domain.MailSender;
import com.study.demo.domain.MailSenderFactory;
import com.study.demo.domain.SimpleMailSender;
import com.study.demo.enums.RetCodeEnum;
import com.study.demo.exception.PayException;
import com.study.demo.utils.FuncUtils;
import com.study.demo.utils.SpringConfigUtil;

/**
 * 邮箱服务
 *
 * @author zhufj
 * @2015-5-28 下午4:24:01
 */
public class EmailService extends BaseService implements IEmailService {
    private static String VERSION = "version";//版本号
    private static String TO_ADDRESS = "to_address";//目标邮箱
    private static String CC_ADDRESS = "cc_address";//抄送人列表
    private static String BCC_ADDRESS = "bcc_address";//密送人列表
    private static String SUBJECT = "subject";//邮件主题
    private static String CONTENT = "content";//邮件正文
    private static String ATTACHMENTS = "attachments";//邮件附件
    private static String FROM_USERNAME = "from_username";//发件人用户名

    /**
     * 发送带附件邮件
     */
    @Override
    public String sendMailWithAttachments(Map<String, Object> params) {
        if (null == params && params.size() == 0) {
            return buildResponse(RetCodeEnum.REQ_PACKAGE_ERROR);
        }
        String correlationID = params.get(CORRELATIONID).toString();
        tag = "correlationID[" + correlationID
                + "]进入邮件服务[发送邮件：sendMailWithAttachments]";
        log.info(tag + "处理方法");
        log.info(tag + "请求报文[" + params.toString() + "]");
        /* step two 必选参数校验 */
        if (FuncUtils.isnull(params.get(VERSION).toString()) ||
                (!SpringConfigUtil.getProperties(VERSION).equals(params.get(VERSION).toString()))) {
            log.info(tag + "发送邮件请求参数[" + VERSION + "]非法");
            return buildResponse(RetCodeEnum.PARAM_ERROR.code,
                    String.format(RetCodeEnum.PARAM_ERROR.msg, VERSION));
        }
        if (FuncUtils.isnull(params.get(SUBJECT).toString())) {
            log.info(tag + "发送邮件请求参数[" + SUBJECT + "]非法");
            return buildResponse(RetCodeEnum.PARAM_ERROR.code,
                    String.format(RetCodeEnum.PARAM_ERROR.msg, SUBJECT));
        }
        if (FuncUtils.isnull(params.get(CONTENT).toString())) {
            log.info(tag + "发送邮件请求参数[" + CONTENT + "]非法");
            return buildResponse(RetCodeEnum.PARAM_ERROR.code,
                    String.format(RetCodeEnum.PARAM_ERROR.msg, CONTENT));
        }
        /* step three 发送邮件 */
        JSONObject resObj = new JSONObject();
        List<String> mailList = (List<String>) params.get(TO_ADDRESS);//主送
        List<String> mailCcList = (List<String>) params.get(CC_ADDRESS);//抄送
        List<String> mailBccList = (List<String>) params.get(BCC_ADDRESS);//暗送
        String subject = params.get(SUBJECT).toString();
        String textContent = params.get(CONTENT).toString();
        Map<String, byte[]> attachments = (Map<String, byte[]>) params.get(ATTACHMENTS);
        String from_username = params.get(FROM_USERNAME).toString();
        MailSender sender = MailSenderFactory.getSenderExt(from_username, log, tag);
        //发送主送与抄送邮件
        if (mailList != null) {
            try {
                log.info(tag + "进入发送邮件服务");
                sender.send(subject, mailList, mailCcList, textContent, attachments);
            } catch (Exception e) {
                log.error(tag + "发送邮件异常：[" + e.getMessage() + "]", e);
                resObj.put(RET_CODE, RetCodeEnum.SYSTEM_ERROR.code);
                resObj.put(RET_MSG, RetCodeEnum.SYSTEM_ERROR.msg);
                log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
                return JSON.toJSONString(resObj);
            }
        } //发送暗送邮件
        if (mailBccList != null) {
            try {
                log.info(tag + "进入发送暗送邮件服务");
                sender.sendBcc(subject, mailBccList, textContent, attachments);
            } catch (Exception e) {
                log.error(tag + "发送暗送邮件异常：[" + e.getMessage() + "]", e);
                resObj.put(RET_CODE, RetCodeEnum.SYSTEM_ERROR.code);
                resObj.put(RET_MSG, RetCodeEnum.SYSTEM_ERROR.msg);
                log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
                return JSON.toJSONString(resObj);
            }
        }
        /* step four 发送报文 */
        resObj.put(RET_CODE, RetCodeEnum.SUCC.code);
        resObj.put(RET_MSG, RetCodeEnum.SUCC.msg);
        log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
        return JSON.toJSONString(resObj);
    }

    /**
     * 发送邮件
     */
    public String sendMail(String reqJSON) {

        /* step one 报文解析开始 */
        JSONObject requestObj = null;
        try {
            requestObj = parseRequest(reqJSON);
        } catch (PayException e) {
            return buildResponse(e.getRet_code(), e.getRet_msg());
        }
        if (null == requestObj) {
            return buildResponse(RetCodeEnum.REQ_PACKAGE_ERROR);
        }
        String correlationID = requestObj.getString(CORRELATIONID);
        tag = "correlationID[" + correlationID
                + "]进入邮件服务[发送邮件：sendMail]";
        log.info(tag + "处理方法");
        log.info(tag + "请求报文[" + reqJSON + "]");
        /* step two 传入参数校验 */
        String errorPara = emailParasValidate(requestObj);
        if (!FuncUtils.isnull(errorPara)) {
            log.info(tag + "发送邮件请求参数[" + errorPara + "]非法");
            return buildResponse(RetCodeEnum.PARAM_ERROR.code,
                    String.format(RetCodeEnum.PARAM_ERROR.msg, errorPara));
        }
        /* step three 发送邮件 */
        JSONObject resObj = new JSONObject();
        List<String> mailList = null;//主送
        List<String> mailCcList = null;//抄送
        List<String> mailBccList = null;//暗送
        if (!FuncUtils.isnull(requestObj.getString(TO_ADDRESS))) {
            mailList = Arrays.asList(requestObj.getString(TO_ADDRESS).split(","));//主送
        }
        if (!FuncUtils.isnull(requestObj.getString(CC_ADDRESS))) {
            mailCcList = Arrays.asList(requestObj.getString(CC_ADDRESS).split(","));//抄送
        }
        if (!FuncUtils.isnull(requestObj.getString(BCC_ADDRESS))) {
            mailBccList = Arrays.asList(requestObj.getString(BCC_ADDRESS).split(","));//暗送
        }
        String subject = requestObj.getString(SUBJECT);
        String content = requestObj.getString(CONTENT);
        String from_username = requestObj.getString(FROM_USERNAME);
        SimpleMailSender sender = MailSenderFactory.getSender(from_username, log, tag);
        //发送主送与抄送邮件
        if (mailList != null) {
            try {
                log.info(tag + "进入发送邮件服务");
                sender.send(mailList, mailCcList, subject, content);
            } catch (Exception e) {
                log.error(tag + "发送邮件异常：[" + e.getMessage() + "]", e);
                resObj.put(RET_CODE, RetCodeEnum.SYSTEM_ERROR.code);
                resObj.put(RET_MSG, RetCodeEnum.SYSTEM_ERROR.msg);
                log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
                return JSON.toJSONString(resObj);
            }
        } //发送暗送邮件
        if (mailBccList != null) {
            try {
                log.info(tag + "进入发送暗送邮件服务");
                sender.sendBcc(mailBccList, subject, content);
            } catch (Exception e) {
                log.error(tag + "发送暗送邮件异常：[" + e.getMessage() + "]", e);
                resObj.put(RET_CODE, RetCodeEnum.SYSTEM_ERROR.code);
                resObj.put(RET_MSG, RetCodeEnum.SYSTEM_ERROR.msg);
                log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
                return JSON.toJSONString(resObj);
            }
        }
        /* step four 发送报文 */
        resObj.put(RET_CODE, RetCodeEnum.SUCC.code);
        resObj.put(RET_MSG, RetCodeEnum.SUCC.msg);
        log.info(tag + "响应报文[" + resObj.toJSONString() + "]");
        return JSON.toJSONString(resObj);
    }

    /**
     * 传入参数校验
     *
     * @param requestObj
     * @return
     */
    private static String emailParasValidate(JSONObject requestObj) {

        // 必选参数
        if (FuncUtils.isnull(requestObj.getString(VERSION)) ||
                (!SpringConfigUtil.getProperties(VERSION).equals(requestObj.getString(VERSION)))) {
            return VERSION;
        }
        if (FuncUtils.isnull(requestObj.getString(SUBJECT))) {
            return SUBJECT;
        }
        if (FuncUtils.isnull(requestObj.getString(CONTENT))) {
            return CONTENT;
        }
        return null;
    }
}
