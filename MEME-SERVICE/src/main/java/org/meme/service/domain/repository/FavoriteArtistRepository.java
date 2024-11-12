package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.FavoriteArtist;
import org.meme.service.domain.entity.FavoritePortfolio;
import org.meme.service.domain.entity.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {
    List<FavoriteArtist> findByModel(Model model);
    boolean existsByModelAndArtist(Model model, Artist artist);
    Optional<FavoriteArtist> findByModelAndArtist(Model model, Artist artist);
    Long countByArtist(Artist artist);

    @Query("SELECT fa " +
            "FROM FavoriteArtist fa " +
            "JOIN FETCH fa.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE fa.model = :model " +
            "ORDER BY fa.createdAt desc")
    Page<FavoriteArtist> findFavoriteArtistByModel(
            @Param(value = "model") Model model,
            Pageable pageable);
}
