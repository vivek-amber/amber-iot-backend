package com.automation.service;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import com.automation.entity.ScheduleEntity;
import com.automation.scheduler.DeviceScheduleJob;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuartzSchedulerService {

    private final Scheduler scheduler;

    public void createJob(ScheduleEntity request) {

        JobDetail jobDetail = JobBuilder.newJob(DeviceScheduleJob.class)
                .withIdentity(request.getId(), "SCHEDULES")
                .usingJobData("scheduleId", request.getId())
                .build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("TRIGGER_" + request.getId(), "SCHEDULES")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cronExpression(request))
                                .inTimeZone(TimeZone.getTimeZone(request.getScheduleTrigger().getTimezone())))
                .build();

        try {

            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to create quartz job", e);
        }
    }

    public void deleteJob(String scheduleId) {

        try {
            scheduler.deleteJob(
                    JobKey.jobKey(scheduleId, "SCHEDULES"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private String cronExpression(ScheduleEntity request) {

        return String.format(
                "0 %d %d ? * %s",
                request.getScheduleTrigger().getMinute(),
                request.getScheduleTrigger().getHour(),
                String.join(",", request.getScheduleTrigger().getRepeatDays()));
    }
}
