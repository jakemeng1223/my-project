package com.jakemeng.lifedb.record;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 只写接口，Spring Data JPA 会根据方法名自动生成 SQL。
 */
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    Optional<DailyRecord> findByRecordDate(LocalDate recordDate);

    List<DailyRecord> findAllByOrderByRecordDateDesc();
}
