package com.study.demo.exception;

import com.study.demo.enums.RetCodeEnum;


/**
 * 
*
* @Description: 自定义异常信息
* @ClassName: PayException 
* @author zhufj
* @date 2019年2月1日 上午10:39:11 
*
 */
public class PayException extends Exception {
	
	private static final long serialVersionUID = 6112585958408901762L;
	//错误码
	private String ret_code;
	//错误信息
	private String ret_msg;
	
	private RetCodeEnum retCodeEnum;

	public PayException(){}
	
	public PayException(String ret_code, String ret_msg){
		this.ret_code = ret_code;
		this.ret_msg = ret_msg;
	}

	public PayException(RetCodeEnum retCodeEnum){
		this.retCodeEnum = retCodeEnum;
		this.ret_code = retCodeEnum.getCode();
		this.ret_msg = retCodeEnum.getMsg();
	}

	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public RetCodeEnum getRetCodeEnum() {
		return retCodeEnum;
	}

	public void setRetCodeEnum(RetCodeEnum retCodeEnum) {
		this.retCodeEnum = retCodeEnum;
	}
	
}
