package org.meme.domain.Repository;

import org.meme.domain.Entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

}
