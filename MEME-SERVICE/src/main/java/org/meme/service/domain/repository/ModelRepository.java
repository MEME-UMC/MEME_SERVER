package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    @Query("SELECT m " +
            "FROM Model m " +
            "JOIN FETCH m.user " +
            "WHERE m.userId = :userId")
    Optional<Model> findModelByUserId(@Param(value = "userId") Long userId);

}
