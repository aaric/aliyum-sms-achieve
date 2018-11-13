package com.github.aaric.achieve.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SmsTest
 *
 * @author Aaric, created on 2018-11-09T10:54.
 * @since 0.0.1-SNAPSHOT
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String product = "Dysmsapi";

    /**
     * 产品域名,开发者无需替换
     */
    private static final String domain = "dysmsapi.aliyuncs.com";

    @Value("${aliyun.dysms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.dysms.accessKeySecret}")
    private String accessKeySecret;

    /**
     * Test Mobile Number
     */
    @Value("${aliyun.dysms.test.number}")
    private String testNumber;

    /**
     * Test Send SMS Callback
     */
    @Value("${aliyun.dysms.test.bizId}")
    private String testBizId;

    @Test
    public void testSendSms() throws Exception {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(testNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("身份验证");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_150740230");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"123456\"}");

        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse response = acsClient.getAcsResponse(request);
        // BizId: 616622542071657945^0, Code: OK, Message: OK
        System.out.println(MessageFormat.format("BizId: {0}, Code: {1}, Message: {2}", response.getBizId(), response.getCode(), response.getMessage()));
    }

    @Test
    public void querySmsStatus() throws Exception {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        // 必填-号码
        request.setPhoneNumber(testNumber);
        // 可选-流水号
        request.setBizId(testBizId);
        // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(dateFormat.format(new Date()));
        // 必填-页大小
        request.setPageSize(10L);
        // 必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        // hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse response = acsClient.getAcsResponse(request);
        System.out.println(MessageFormat.format("Code: {0}, Message: {1}", response.getCode(), response.getMessage()));
        // 输出详细信息
        for (QuerySendDetailsResponse.SmsSendDetailDTO detailDTO : response.getSmsSendDetailDTOs()) {
            System.out.println("## Sms Send Detail");
            System.out.println("Content=" + detailDTO.getContent());
            System.out.println("ErrCode=" + detailDTO.getErrCode());
            System.out.println("OutId=" + detailDTO.getOutId());
            System.out.println("PhoneNum=" + detailDTO.getPhoneNum());
            System.out.println("ReceiveDate=" + detailDTO.getReceiveDate());
            System.out.println("SendDate=" + detailDTO.getSendDate());
            System.out.println("SendStatus=" + detailDTO.getSendStatus());
            System.out.println("Template=" + detailDTO.getTemplateCode());
        }
    }
}
