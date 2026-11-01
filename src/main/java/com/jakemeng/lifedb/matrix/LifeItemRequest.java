package com.jakemeng.lifedb.matrix;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LifeItemRequest(
        @NotNull(message = "要选一个方面") LifeDomain domain,
        @NotBlank(message = "名称不能为空") @Size(max = 50, message = "名称最多 50 字") String name,
        @Size(max = 500, message = "当前情况最多 500 字") String detail,
        ItemStatus status,
        Priority priority,
        @Size(max = 500, message = "下一步最多 500 字") String nextStep) {
}
