package org.meme.service.domain.repository;

import org.meme.service.domain.entity.FavoriteArtist;
import org.meme.service.domain.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {
    List<FavoriteArtist> findByModel(Model model);
    boolean existsByModelAndArtistId(Model model, Long userId);
    Optional<FavoriteArtist> findByModelAndArtistId(Model model, Long userId);
    Long countByArtistId(Long artistId);
}
