package com.github.aaric.achieve.sms.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SmsServiceTest
 *
 * @author Aaric, created on 2018-11-13T09:51.
 * @since 0.1.0-SNAPSHOT
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsServiceTest {

    @Autowired
    private SmsService smsService;

    /**
     * Test Mobile Number
     */
    @Value("${aliyun.dysms.test.number}")
    private String testNumber;

    @Test
    public void testSendSms() {
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("code", MessageFormat.format("{0,number,000000}", new Random().nextInt(999999)));

        String bizId = smsService.sendSms("身份验证", "SMS_150740230", templateParams, null, testNumber);
        System.out.println("bizId: " + bizId);
        Assert.assertNotNull(bizId);
    }
}
