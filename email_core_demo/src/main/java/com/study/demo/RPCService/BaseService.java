package com.study.demo.RPCService;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.demo.IBaseService;
import com.study.demo.enums.RetCodeEnum;
import com.study.demo.exception.PayException;
import com.study.demo.utils.FuncUtils;

/**
 * 
*
* @Description: 
* @ClassName: BaseService 
* @author zhufj
* @date 2019年2月1日 上午10:39:25 
*
 */
public class BaseService implements IBaseService{
    protected Logger              log              = Logger
                                                           .getLogger(getClass());
    protected String tag = "";
	protected static final String RET_CODE = "ret_code";
	protected static final String RET_MSG = "ret_msg";
	protected static final String CORRELATIONID = "correlationID";
	
	/**
	 * 请求报文解析
	 * 
	 * @param reqJSON
	 * @return
	 * @throws PayException
	 */
	public JSONObject parseRequest(String reqJSON) throws PayException {
		if (FuncUtils.isnull(reqJSON)) {
			throw new PayException(RetCodeEnum.REQ_PACKAGE_ERROR);
		}
		JSONObject requestObj = null;
		try {
			requestObj = JSON.parseObject(reqJSON);
		} catch (Exception e) {
			log.error("请求报文[" + reqJSON + "]解析异常:" + e.getMessage(), e);
			throw new PayException(RetCodeEnum.REQ_PACKAGE_ERROR);
		}
		if (FuncUtils.isnull(requestObj.getString(CORRELATIONID))) {
			throw new PayException(RetCodeEnum.PARAM_ERROR.code, String.format(
					RetCodeEnum.PARAM_ERROR.msg, CORRELATIONID));
		}
		return requestObj;
	}
	/**
	 * 构造返回报文
	 * 
	 * @param ret_code
	 * @param ret_msg
	 * @return
	 */
	public String buildResponse(String ret_code, String ret_msg) {
		JSONObject res = new JSONObject();
		res.put(RET_CODE, ret_code);
		res.put(RET_MSG, ret_msg);
		log.info(tag + "响应报文[" + res.toJSONString() + "]");
		return res.toJSONString();
	}

	/**
	 * 构造返回报文
	 * 
	 * @param ret_code
	 * @param ret_msg
	 * @return
	 */
	public String buildResponse(RetCodeEnum retCodeEnum) {
		return buildResponse(retCodeEnum.code, retCodeEnum.msg);
	}
}
