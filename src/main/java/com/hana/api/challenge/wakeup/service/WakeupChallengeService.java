package com.hana.api.challenge.wakeup.service;

import com.hana.api.challenge.wakeup.dto.WakeupResponseDto;
import com.hana.api.challenge.wakeup.repository.WakeupChallengeRepository;
import com.hana.api.group.entity.Group;
import com.hana.api.groupMember.repository.GroupMemberRepository;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WakeupChallengeService {

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final WakeupChallengeRepository wakeupChallengeRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    public static String getTodayObject() {
        List<String> detectList = Arrays.asList("toothbrush", "book", "clock", "spoon", "hair drier");

        // 랜덤하게 리스트에서 하나의 스트링 객체 선택
        Random rand = new Random();
        String selectedObject = detectList.get(rand.nextInt(detectList.size()));

        return selectedObject;
    }

    public void postWakeupCertificate(UUID userCode) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));
        String userId = user.getPhoneNumber();
        Long groupId = groupMemberRepository.findGroupByUserId(userId);
        if (groupId == null) {
            throw new BaseException(BaseResponseStatus.GROUPS_USER_NOT_FOUND);
        }

        WakeupResponseDto.WakeupCertificateDto certificate = new WakeupResponseDto.WakeupCertificateDto();
        certificate.setWakeupTime(LocalDateTime.now());
        certificate.setUserId(userId);
        String key = "wakeup:certificate:group:" + groupId + ":userId:" + userId;
        // Redis에 26시간 동안 유지
        redisTemplate.opsForValue().set(key, certificate, 26, TimeUnit.HOURS);
    }

//    public WakeupResponseDto.WakeupCertificateDto getWakeupCertificate(UUID userCode) {
//        User user = userRepository.findByUserCode(userCode)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));
//        String userId = user.getPhoneNumber();
//        Long groupId = groupMemberRepository.findGroupByUserId(userId);
//
//        String key = "wakeup:certificate:group:" + groupId + ":userId:" + userId;
//        return (WakeupResponseDto.WakeupCertificateDto) redisTemplate.opsForValue().get(key);
//    }
//
//    public void deleteWakeupCertificate(UUID userCode) {
//        User user = userRepository.findByUserCode(userCode)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));
//
//        String userId = user.getPhoneNumber();
//        Long groupId = groupMemberRepository.findGroupByUserId(userId);
//
//        String key = "wakeup:certificate:group:" + groupId + ":userId:" + userId;
//        redisTemplate.delete(key);
//    }


}
