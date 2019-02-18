package com.github.aaric.achieve.sms.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 短信消息
 *
 * @author Aaric, created on 2018-01-18T10:56.
 * @since 2.1-SNAPSHOT
 */
public class SmsMsg {

    /**
     * 模板编码-验证码
     */
    public static final String SMS_TEMPLATE_CODE_VALIDATE = "SMS_150740230";

    /**
     * 短信模板信息
     */
    public static Map<String, String> SMS_TEMPLATE_INFO;

    static {
        // 添加短信模板信息
        SMS_TEMPLATE_INFO = new HashMap<>();
        // 验证码
        SMS_TEMPLATE_INFO.put(SMS_TEMPLATE_CODE_VALIDATE, "您的验证码为${code}，两分钟内有效。如非本人操作，请忽略本短信。");
    }

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 模板编号
     */
    private String templateCode;

    /**
     * 模板参数信息
     */
    private Map<String, String> templateParams;

    public SmsMsg() {
    }

    public SmsMsg(String mobile, String templateCode, Map<String, String> templateParams) {
        this.mobile = mobile;
        this.templateCode = templateCode;
        this.templateParams = templateParams;
    }

    /**
     * 获得短信内容
     *
     * @return
     */
    public String getContent() {
        if (null != this.templateCode && null != this.templateParams) {
            String content = SMS_TEMPLATE_INFO.get(this.templateCode);
            if (StringUtils.isNotBlank(content)) {
                Set<Map.Entry<String, String>> params = this.templateParams.entrySet();
                for (Map.Entry<String, String> object : params) {
                    content = content.replace("${" + object.getKey() + "}", object.getValue());
                }
                return content;
            }
        }
        return null;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
