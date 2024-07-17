package org.meme.domain.repository;

import org.meme.domain.entity.ArtistEnableTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistEnableTimeRepository extends JpaRepository<ArtistEnableTime, Long> {
    Optional<ArtistEnableTime> findByArtist_UserId(Long artistId);
}
