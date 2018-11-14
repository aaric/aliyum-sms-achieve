package com.github.aaric.achieve.sms.service;

import java.util.Map;

/**
 * 发送短信接口
 *
 * @author Aaric, created on 2018-11-13T09:46.
 * @since 0.1.0-SNAPSHOT
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param signName       发送内容
     * @param templateCode   短信模板
     * @param templateParams 模板中的变量参数
     * @param outId          外部流水扩展字段
     * @param tos            发送人
     * @return 发送回执ID
     */
    String sendSms(String signName, String templateCode, Map<String, String> templateParams, String outId, String... tos);
}
