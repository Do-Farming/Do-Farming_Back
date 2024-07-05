package com.hana.common.scheduler;

import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Service
public class DynamicSchedulerService {

    private final TaskScheduler taskScheduler;
    private final GroupRepository groupRepository;

    @Autowired
    public DynamicSchedulerService(TaskScheduler taskScheduler, GroupRepository groupRepository) {
        this.taskScheduler = taskScheduler;
        this.groupRepository = groupRepository;
    }

    public void scheduleStatusUpdate(long groupId) {
        // 현재로부터 차주 월요일을 계산
        LocalDateTime nextMonday = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        // 차주 월요일로부터 3개월 후의 날짜 계산
        LocalDateTime threeMonthsLater = nextMonday.plusMonths(3);

        // 스케줄링할 작업 정의
        Runnable task = () -> {
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID));
            group.setStatus(2);
            group.setStartedAt(LocalDate.from(nextMonday));
            group.setEndedAt(LocalDate.from(threeMonthsLater));
            groupRepository.save(group);
        };

        // 차주 월요일에 작업 스케줄링
        Date scheduleDate = Date.from(nextMonday.atZone(java.time.ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(task, scheduleDate);
    }
}
