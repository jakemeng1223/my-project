package com.jakemeng.lifedb.record;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 前端提交的数据。和实体类分开，前端就改不了 id、创建时间这类字段。
 */
public record DailyRecordRequest(
        LocalTime sleepTime,
        LocalTime wakeTime,
        @Min(value = 1, message = "情绪最低 1 分") @Max(value = 10, message = "情绪最高 10 分") Integer mood,
        @Size(max = 500, message = "饮食最多 500 字") String meals,
        @DecimalMin(value = "0", message = "开销不能是负数") BigDecimal spending,
        @Size(max = 2000, message = "做了什么最多 2000 字") String activities,
        @Size(max = 2000, message = "备注最多 2000 字") String note) {
}
