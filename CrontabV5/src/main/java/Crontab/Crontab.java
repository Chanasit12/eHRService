package Crontab;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Crontab {

    private static JobDetail createJob(Worker worker, String identity) throws ClassNotFoundException, NoSuchMethodException {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(worker);
        jobDetail.setTargetMethod("Add_Checkin");
        jobDetail.setName(identity);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        return jobDetail.getObject();
    }

    private static Trigger createTrigger(String identity) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        System.out.println("Time "+Time);
        CronExpression cx = new CronExpression("0 0 6 ? * MON,TUE,WED,THU,FRI *");
        CronScheduleBuilder atHourAndMinuteOnGivenDaysOfWeek = CronScheduleBuilder.cronSchedule(cx);
        return TriggerBuilder.newTrigger()
                .withSchedule(atHourAndMinuteOnGivenDaysOfWeek) //set เวลา
                .withIdentity(identity) //ระบุคีย์ trigger โดยจะต้องไม่ซ้ำกัน
                .startNow()
                .build();
    }

    public static void main(String[] args) {
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();
            Worker worker = new Worker();
            JobDetail job = createJob(worker, "workJob1");
            Trigger trigger = createTrigger("workTrigger1");
            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
