package com.hana.api.challenge.service;

import com.hana.api.challenge.dto.response.QuizResponseDto;
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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuizChallengeService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private static final String QUIZ_TIME_KEY = "quizTime:";

    public void storeQuizChallengeData(UUID userCode, int score, long completionTime) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));
        String userId = user.getPhoneNumber();
        Long groupId = groupMemberRepository.findGroupByUserId(userId);
        if (groupId == null) {
            throw new BaseException(BaseResponseStatus.GROUPS_USER_NOT_FOUND);
        }

        String key = QUIZ_TIME_KEY + userId;
        QuizResponseDto quizResponseDto = new QuizResponseDto(score, completionTime);
        redisTemplate.opsForValue().set(key, quizResponseDto, 26, TimeUnit.HOURS);
    }
}