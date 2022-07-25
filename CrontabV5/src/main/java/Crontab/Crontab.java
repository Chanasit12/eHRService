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
        jobDetail.setTargetMethod("Add_Leavecount");
        jobDetail.setName(identity);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        return jobDetail.getObject();
    }

    private static Trigger createTrigger(String identity) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        CronExpression cx = new CronExpression("0 0 0 1 1 ?");
        CronScheduleBuilder atHourAndMinuteOnGivenDaysOfWeek = CronScheduleBuilder.cronSchedule(cx);
        return TriggerBuilder.newTrigger()
                .withSchedule(atHourAndMinuteOnGivenDaysOfWeek)
                .withIdentity(identity)
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
