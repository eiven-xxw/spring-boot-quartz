package com.yang.quartz.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Copyright: Copyright (c) 2016,${year},
 * @Description: ${todo}
 */

public class ScheduledJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public ScheduledJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        //doSomething
        System.out.println(dataMap.getString("className")+"...................................");
        System.out.println(dataMap.getString("methodName")+"...................................");
    }
}
