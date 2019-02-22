package com.study.demo.enums;

/**
 *  交易结果返回枚举 
 * @author zhufj
 * @2015-5-28 下午3:07:40
 */
public enum RetCodeEnum
{
    // 公共
    SUCC("0000", "交易成功"), 
    SYSTEM_ERROR("9999", "系统繁忙，稍后重试"),
    REQ_PACKAGE_ERROR("1001", "请求报文非法"), 
    PARAM_ERROR("1002", "请求参数[%s]错误"),
    ;
    public final String code;
    public final String msg;

    RetCodeEnum(String code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
}
