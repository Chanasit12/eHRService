package th.co.techberry.Crontab;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

public class Crontab {

    private static JobDetail createJob(Worker worker, String identity) throws ClassNotFoundException, NoSuchMethodException {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(worker);
        jobDetail.setTargetMethod("Add_Checkin");
        //ระบุคีย์  โดยจะต้องไม่ซ้ำกัน
        jobDetail.setName(identity);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        return jobDetail.getObject();
    }

//    private static Trigger createTrigger(String identity)
//            throws ParseException {
//        //CronExpression cx = new CronExpression("0/5 * * * * ?");
//        Integer[] days = {2, 3, 4, 5, 6}; //1 คือ วันอาทิตย์ 7 คือวันเสาร์  จะใส่กี่ตัวก็ได้
//        CronScheduleBuilder atHourAndMinuteOnGivenDaysOfWeek = CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(6,0,days);
//        return TriggerBuilder.newTrigger()
//                .withSchedule(atHourAndMinuteOnGivenDaysOfWeek)
//                .withIdentity(identity)
//                .startNow()
//                .build();
//    }

    private static Trigger createTrigger(String identity) throws ParseException {
        //ตั้งเวลา  จากตัวอย่างคือ ทุกๆ 5 วินาที
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
            //get schedule
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();

            //สร้าง object หรือ งานที่จะให้ ทำตาม scheduler
            Worker worker = new Worker();

            //สร้าง job detail เพื่อบอกรายละเอียดงานที่จะให้ทำ พร้อมระบุ id ของ job นั้น  โดยจะต้องมี id ไม่ซ้ำกันด้วย
            JobDetail job = createJob(worker,"workJob1");

            //สร้าง trigger คือตัวตั้งเวลาการทำงาน  ว่าจะให้ทำตอนไหน พร้อมระบุ id ของ trigger นั้น
//            Trigger trigger = createTrigger("workTrigger1");
            Trigger trigger = createTrigger("workTrigger1");

            //นำ job และ trigger ยัดลงใน schedule
            sched.scheduleJob(job,trigger);
            //start งานนั้น  ให้เริ่มทำงาน
            sched.start();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
