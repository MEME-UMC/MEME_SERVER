package org.meme.domain.repository;

import org.meme.domain.entity.FavoriteArtist;
import org.meme.domain.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {
    List<FavoriteArtist> findByModel(Model model);
    boolean existsByModelAndArtistId(Model model, Long userId);
    Optional<FavoriteArtist> findByModelAndArtistId(Model model, Long userId);
    Long countByArtistId(Long artistId);
}
