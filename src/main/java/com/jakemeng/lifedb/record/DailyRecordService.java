package com.jakemeng.lifedb.record;

import com.jakemeng.lifedb.common.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyRecordService {

    private final DailyRecordRepository repository;

    public DailyRecordService(DailyRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<DailyRecord> list() {
        return repository.findAllByOrderByRecordDateDesc();
    }

    @Transactional(readOnly = true)
    public DailyRecord get(LocalDate date) {
        return repository.findByRecordDate(date)
                .orElseThrow(() -> new NotFoundException(date + " 还没有记录"));
    }

    /** 这一天有记录就更新，没有就新建。 */
    @Transactional
    public DailyRecord save(LocalDate date, DailyRecordRequest request) {
        DailyRecord record = repository.findByRecordDate(date).orElseGet(() -> new DailyRecord(date));
        record.setSleepTime(request.sleepTime());
        record.setWakeTime(request.wakeTime());
        record.setMood(request.mood());
        record.setMeals(request.meals());
        record.setSpending(request.spending());
        record.setActivities(request.activities());
        record.setNote(request.note());
        return repository.save(record);
    }

    @Transactional
    public void delete(LocalDate date) {
        repository.delete(get(date));
    }
}
