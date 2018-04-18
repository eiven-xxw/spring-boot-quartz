package com.yang.quartz.controller;

import com.yang.quartz.entity.ScheduleEntity;
import com.yang.quartz.service.ScheduleJobService;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2016/5/31 0031.
 */
@Controller
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 任务列表
     *
     * @return
     */

    @RequestMapping("/index")
    public String list() {
        return "/index";
    }

    @ResponseBody
    @RequestMapping("list.json")
    public Map<String, Object> getAllJobs(){
        List<ScheduleEntity> scheduleEntities = scheduleJobService.getAllScheduleJob();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", scheduleEntities);
        return map;
    }
    @RequestMapping("add")
    public String add() {
        return "/add";
    }

    @ResponseBody
    @RequestMapping("/save.json")
    public Object create(ScheduleEntity scheduleEntity) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        // 判断表达式


        boolean f = CronExpression.isValidExpression(scheduleEntity
                .getCronExpression());
        if (!f) {
            map.put("msg", "cron表达式有误，不能被解析！");
            return map;
        }
        try {
            scheduleEntity.setStatus("1");
            scheduleJobService.add(scheduleEntity);
            map.put("status", 0);
            map.put("msg","保存成功!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            map.put("msg", "找不到指定的类");
        } catch (SchedulerException e) {
            e.printStackTrace();
            if (e.getMessage().contains(
                    "because one already exists with this identification")) {
                map.put("msg", "任务组中存在同样的任务名");
            } else {
                map.put("msg", "未知原因,添加任务失败");
            }
        } catch (Exception e) {
            map.put("msg", "系统错误!");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 暂停任务
     */

    @ResponseBody
    @RequestMapping("/stopJob")
    public Object stop(String jobName, String jobGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        try {
            scheduleJobService.stopJob(jobName, jobGroup);
            map.put("status", 0);
            map.put("msg","暂停成功!");
        } catch (SchedulerException e) {
            e.printStackTrace();
            map.put("msg", "系统错误,请联系管理员!");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(String jobName, String jobGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        try {
            scheduleJobService.delJob(jobName, jobGroup);
            map.put("status", 0);
            map.put("msg","删除成功!");
        } catch (SchedulerException e) {
            e.printStackTrace();
            map.put("msg", "系统错误,请联系管理员!");
        }
        return map;
    }

    /**
     * 修改表达式
     */

    @ResponseBody
    @RequestMapping("/update")
    public Object update(ScheduleEntity scheduleEntity) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        // 验证cron表达式


        boolean f = CronExpression.isValidExpression(scheduleEntity
                .getCronExpression());
        if (!f) {
            map.put("msg", "cron表达式有误，不能被解析！");
            return map;
        }
        try {
            scheduleJobService.modifyTrigger(scheduleEntity.getJobName(),
                    scheduleEntity.getJobGroup(), scheduleEntity.getCronExpression());
            map.put("status", 0);
        } catch (SchedulerException e) {
            e.printStackTrace();
            map.put("msg", "系统错误,请联系管理员!");
        }
        return map;
    }

    /**
     * 立即运行一次
     */

    @ResponseBody
    @RequestMapping("/startNow")
    public Object stratNow(String jobName, String jobGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        try {
            scheduleJobService.startNowJob(jobName, jobGroup);
            map.put("status", 0);
        } catch (SchedulerException e) {
            e.printStackTrace();
            map.put("msg", "系统错误,请联系管理员!");
        }
        return map;
    }

    /**
     * 恢复
     */

    @ResponseBody
    @RequestMapping("/resume")
    public Object resume(String jobName, String jobGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        try {
            scheduleJobService.restartJob(jobName, jobGroup);
            map.put("status", 0);
            map.put("msg","恢复成功!");
        } catch (SchedulerException e) {
            e.printStackTrace();
            map.put("msg", "系统错误,请联系管理员!");
        }
        return map;
    }

    /**
     * 获取所有trigger
     */
    public void getTriggers(HttpServletRequest request) {
        List<ScheduleEntity> scheduleEntities = scheduleJobService.getTriggersInfo();
        request.setAttribute("triggers", scheduleEntities);
    }
}
