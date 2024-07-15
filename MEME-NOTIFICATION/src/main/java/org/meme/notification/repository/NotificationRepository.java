package org.meme.notification.repository;

import org.meme.notification.entity.NotificationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NotificationRepository extends ElasticsearchRepository<NotificationDocument, Long> {

    List<NotificationDocument> findByUserId(Long userId);
}
