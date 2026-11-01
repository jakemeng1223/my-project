package com.jakemeng.lifedb.matrix;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * 矩阵里的一项，比如"身体 - 睡眠 - 较差"。
 */
@Entity
@Table(name = "life_item")
public class LifeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 存枚举的名字而不是序号，以后调整枚举顺序也不会把数据弄乱 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LifeDomain domain;

    @Column(nullable = false, length = 50)
    private String name;

    /** 当前情况 */
    @Column(length = 500)
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ItemStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority;

    /** 下一步打算怎么改 */
    @Column(length = 500)
    private String nextStep;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LifeDomain getDomain() {
        return domain;
    }

    public void setDomain(LifeDomain domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
