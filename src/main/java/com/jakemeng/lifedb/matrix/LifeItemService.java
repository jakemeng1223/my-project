package com.jakemeng.lifedb.matrix;

import com.jakemeng.lifedb.common.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifeItemService {

    private final LifeItemRepository repository;

    public LifeItemService(LifeItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LifeItem> list() {
        return repository.findAllByOrderByDomainAscIdAsc();
    }

    @Transactional
    public LifeItem create(LifeItemRequest request) {
        LifeItem item = new LifeItem();
        apply(item, request);
        return repository.save(item);
    }

    @Transactional
    public LifeItem update(Long id, LifeItemRequest request) {
        LifeItem item = get(id);
        apply(item, request);
        return item;
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(get(id));
    }

    private LifeItem get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("找不到这一项"));
    }

    private static void apply(LifeItem item, LifeItemRequest request) {
        item.setDomain(request.domain());
        item.setName(request.name().strip());
        item.setDetail(request.detail());
        item.setStatus(request.status());
        item.setPriority(request.priority());
        item.setNextStep(request.nextStep());
    }
}
