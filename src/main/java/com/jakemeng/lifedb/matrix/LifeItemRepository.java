package com.jakemeng.lifedb.matrix;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeItemRepository extends JpaRepository<LifeItem, Long> {

    List<LifeItem> findAllByOrderByDomainAscIdAsc();
}
