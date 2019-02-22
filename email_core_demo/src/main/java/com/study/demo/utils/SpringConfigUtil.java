package com.study.demo.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * spring通过配置获取properties文件中的内容
 *
 * @author wangyh
 */
public class SpringConfigUtil extends PropertyPlaceholderConfigurer {
    private static Logger log = Logger.getLogger(SpringConfigUtil.class);
    /**
     * 自定义map,用来装载property初始化后的键值对
     */
    private static Map<String, Object> ctxPropertiesMap;

    /**
     * 重载该方法，将文件中键值对放入map中
     */
    @Override
    protected void processProperties(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, Object>();
        //
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value); // 放入map
        }
    }

    /**
     * 通过key值从map中获取对应的value
     *
     * @param name
     * @return
     */
    public static Object getContextProperty(String name) {
        return ctxPropertiesMap.get(name);
    }

    /**
     * 获取property中key对应的值
     *
     * @param key
     * @return
     */
    public static String getProperties(String key) {
        Object value = getContextProperty(key);
        if (null == value) {
            log.warn("key:" + key + " value is null");
            return "";
        }
        String valueStr = (String) value;
        return valueStr;
    }

    /**
     * 获取值并设置参数值
     *
     * @param key
     * @param param
     * @return
     */
    public static String setProperties(final String key, String param) {
        String msg = getProperties(key);
        return MessageFormat.format(msg, param);
    }
}
