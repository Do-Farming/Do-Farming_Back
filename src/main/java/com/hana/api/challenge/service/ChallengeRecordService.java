package com.hana.api.challenge.service;

import com.hana.api.card.entity.Card;
import com.hana.api.challenge.entity.ChallengeRecord;
import com.hana.api.challenge.repository.ChallengRecordRepository;
import com.hana.api.challenge.repository.ChallengeRecordRepository;
import com.hana.api.firebase.service.PushNotificationService;
import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.groupMember.entity.GroupMember;
import com.hana.api.groupMember.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeRecordService {

    final ChallengRecordRepository challengRecordRepository;
    final ChallengeRecordRepository challengeRecordRepository;
    final GroupRepository groupRepository;
    final GroupMemberRepository groupMemberRepository;
    final PushNotificationService pushNotificationService;

    public int getChallenge(){
        return challengeRecordRepository.findByChallengeDate(LocalDate.now()).getChallengeType();
    }


    @Transactional
    public int insertDailyChallengeRecord() {
        LocalDate today = LocalDate.now();
        int randomNumber = (int) (Math.random() * 3);
        ChallengeRecord challengeRecord = ChallengeRecord.builder()
                .challengeDate(today.plusDays(1))
                .challengeType(randomNumber).build();
        challengRecordRepository.save(challengeRecord);
        String challenge = "";
        switch (randomNumber){
            case 0:
                challenge = "Walking";
                break;
            case 1:
                challenge = "Wake up At Morning";
                break;
            case 2:
                challenge = "Quiz Time";
                break;
        }
        List<Group> groups = groupRepository.findActiveGroup(LocalDate.now());
        for(Group g : groups){
            log.info(g.toString());
            List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroupId(g.getId());
            for(GroupMember gm : groupMembers){
                String deviceId = gm.getUser().getDeviceId();
                pushNotificationService.sendNotificationToDevice(deviceId, "Check your Chellenge", challenge);
            }
        }
        return randomNumber;
    }
}
