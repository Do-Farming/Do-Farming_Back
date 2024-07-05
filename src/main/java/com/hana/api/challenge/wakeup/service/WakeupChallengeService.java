package com.hana.api.challenge.wakeup.service;

import com.hana.api.challenge.wakeup.repository.WakeupChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WakeupChallengeService {

    private final WakeupChallengeRepository wakeupChallengeRepository;

    public static String getTodayObject() {
        List<String> detectList = Arrays.asList("toothbrush", "book", "clock", "spoon", "hair drier");

        // 랜덤하게 리스트에서 하나의 스트링 객체 선택
        Random rand = new Random();
        String selectedObject = detectList.get(rand.nextInt(detectList.size()));


        return selectedObject;
    }

}
