package com.hana.api.sms.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.hana.api.sms.repository.SmsReservationRepository;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import net.nurigo.sdk.message.model.Message;


@Service
public class SmsService {

    private final DefaultMessageService defaultMessageService;
    private final SmsReservationRepository smsReservationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String CERTIFICATION = "[본인 인증 번호] : ";

    public SmsService(@Value("${coolsms.apikey}") String apiKey, @Value("${coolsms.apiPrivate}") String apiSecret,
                      RedisTemplate<String, Object> redisTemplate, SmsReservationRepository smsReservationRepository) {
        this.redisTemplate = redisTemplate;
        this.smsReservationRepository = smsReservationRepository;
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey,
                apiSecret, "https://api.coolsms.co.kr");
    }

    public Boolean sendCertificationMessage(String phoneNumber) {
        String certification = createCertificationNumber();
        String text = CERTIFICATION + certification + " 입니다.";
        sendMessage(phoneNumber, text);
        redisTemplate.opsForValue().set(phoneNumber, certification, 5, TimeUnit.MINUTES);
        return Boolean.TRUE;
    }

    public void sendMessage(String phoneNumber, String text) {
        Message message = new Message();
        message.setFrom("01080195950");
        message.setTo(phoneNumber);
        message.setText(text);

        try {
            defaultMessageService.send(message);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException |
                 NurigoUnknownException e) {
            throw new RuntimeException(e);
        }
    }


    private String createCertificationNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000);
    }

}
