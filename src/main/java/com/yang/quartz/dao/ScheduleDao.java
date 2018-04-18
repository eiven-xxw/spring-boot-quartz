package com.yang.quartz.dao;

import com.yang.quartz.entity.ScheduleEntity;
import com.yang.quartz.pagePlugin.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public interface ScheduleDao {
    int insert(ScheduleEntity scheduleEntity);

    List<ScheduleEntity> listPage(@Param("pagination")Pagination pagination, @Param("scheduleEntity")ScheduleEntity scheduleEntity);
}
