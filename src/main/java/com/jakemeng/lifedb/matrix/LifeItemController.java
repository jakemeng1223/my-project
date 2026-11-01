package com.jakemeng.lifedb.matrix;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发展矩阵的接口。和每日记录不同，矩阵的项没有天然的唯一标识，所以新建用 POST，由数据库生成 id。
 */
@RestController
@RequestMapping("/api/items")
public class LifeItemController {

    private final LifeItemService service;

    public LifeItemController(LifeItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<LifeItem> list() {
        return service.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LifeItem create(@Valid @RequestBody LifeItemRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public LifeItem update(@PathVariable Long id, @Valid @RequestBody LifeItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
