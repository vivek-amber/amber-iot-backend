package com.automation.scheduler;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.automation.dto.CommandDto;
import com.automation.entity.ScheduleEntity;
import com.automation.exception.ScheduleNotFoundException;
import com.automation.repository.ScheduleRepository;
import com.automation.service.KafkaProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceScheduleJob implements Job {

    private final ScheduleRepository scheduleRepository;

    private final KafkaProducerService kafkaProducerService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String scheduleId = context.getMergedJobDataMap().getString("scheduleId");

        ScheduleEntity schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("No schedule found"));

        schedule.getDevice()
                .getActions()
                .forEach(s -> kafkaProducerService.sendMessage(new CommandDto(schedule.getCompanyId(),
                        schedule.getDevice().getDeviceId(), s.getCmd(), s.getVal())));

    }

}
