package com.study.demo.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 基本工具
 * @author zhufj
 * @2015-5-28 下午4:02:24
 */
public class FuncUtils
{
  public static boolean isnull(String str) {
    if ((str == null) || (str.equalsIgnoreCase("null")) || (str.equals(""))) {
      return true;
    }
    return false;
  }
  /**  
   * 获取唯一标识uuid
   *       
   * @return  
   */ 
  public static String getUuid()
  {     
      UUID uuid = UUID.randomUUID();
      return uuid.toString();
  }
  /**
   * 邮箱校验
   * @param str
   * @return
   */
  public static boolean checkEmail(String str)
  {
    boolean tem = false;
    String reg = "\\w+\\@\\w+\\.(com|cn|com.cn|net|org|gov|gov.cn|edu|edu.cn)";
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(str);
    tem = matcher.matches();
    return tem;
  }

}