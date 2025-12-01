package com.smalldogg.whereiam.domain.outbox;

import com.smalldogg.whereiam.domain.outbox.entity.OutboxEvent;
import com.smalldogg.whereiam.domain.outbox.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop100ByStatusOrderBySeqAsc(OutboxStatus status);
}
