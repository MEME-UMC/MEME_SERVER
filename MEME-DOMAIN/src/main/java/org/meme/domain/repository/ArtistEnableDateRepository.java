package org.meme.domain.repository;

import org.meme.domain.entity.ArtistEnableDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistEnableDateRepository extends JpaRepository<ArtistEnableDate, Long> {
    Optional<ArtistEnableDate> findByArtist_UserId(Long artistId);
}
