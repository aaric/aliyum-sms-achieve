package com.github.aaric.achieve.sms.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.aaric.achieve.sms.service.SmsService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 发送短信接口实现
 *
 * @author Aaric, created on 2018-11-13T09:48.
 * @since 0.1.0-SNAPSHOT
 */
@Service
public class SmsServiceImpl implements SmsService {

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String ALIYUN_SMS_PRODUCT = "Dysmsapi";

    /**
     * 产品域名,开发者无需替换
     */
    private static final String ALIYUN_SMS_DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 默认超时时间
     */
    private static final String ALIYUN_SMS_DEFAULT_TIMEOUT = "10000";

    /**
     * 服务地址
     */
    private static final String ALIYUN_SMS_ENDPOINT = "cn-hangzhou";

    /**
     * 服务器ID
     */
    private static final String ALIYUN_SMS_REGIONID = "cn-hangzhou";

    /**
     * 授权KEY
     */
    @Value("${aliyun.dysms.accessKeyId}")
    private String accessKeyId;

    /**
     * 授权密钥
     */
    @Value("${aliyun.dysms.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 短信签名
     */
    @Value("${aliyun.dysms.signName}")
    private String signName;

    @Override
    public String sendSms(String templateCode, Map<String, String> templateParams, String outId, String... tos) {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", ALIYUN_SMS_DEFAULT_TIMEOUT);
        System.setProperty("sun.net.client.defaultReadTimeout", ALIYUN_SMS_DEFAULT_TIMEOUT);

        try {
            // 初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile(ALIYUN_SMS_REGIONID, accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint(ALIYUN_SMS_ENDPOINT, ALIYUN_SMS_REGIONID, ALIYUN_SMS_PRODUCT, ALIYUN_SMS_DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            // 组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            // 必填:待发送手机号
            request.setPhoneNumbers(StringUtils.join(tos, ","));
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            // 必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(new Gson().toJson(templateParams));
            // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId(outId);

            // hint 此处可能会抛出异常，注意catch
            SendSmsResponse response = acsClient.getAcsResponse(request);
            // BizId: 616622542071657945^0, Code: OK, Message: OK
            if (null != response && StringUtils.equals("OK", response.getCode())) {
                return response.getBizId();
            }

        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
