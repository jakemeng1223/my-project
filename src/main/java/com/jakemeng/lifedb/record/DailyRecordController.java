package com.jakemeng.lifedb.record;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每日记录的 REST 接口。日期放在路径里，格式 yyyy-MM-dd，例如 /api/records/2026-11-01。
 */
@RestController
@RequestMapping("/api/records")
public class DailyRecordController {

    private final DailyRecordService service;

    public DailyRecordController(DailyRecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<DailyRecord> list() {
        return service.list();
    }

    @GetMapping("/{date}")
    public DailyRecord get(@PathVariable LocalDate date) {
        return service.get(date);
    }

    @PutMapping("/{date}")
    public DailyRecord save(@PathVariable LocalDate date, @Valid @RequestBody DailyRecordRequest request) {
        return service.save(date, request);
    }

    @DeleteMapping("/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable LocalDate date) {
        service.delete(date);
    }
}
