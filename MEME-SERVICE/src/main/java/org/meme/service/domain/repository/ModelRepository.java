package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {

    Optional<Model> findByEmail(String email);
}
