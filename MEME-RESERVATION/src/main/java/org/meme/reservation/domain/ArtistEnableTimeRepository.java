package org.meme.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistEnableTimeRepository extends JpaRepository<ArtistEnableTime, Long> {
    Optional<ArtistEnableTime> findByUserId(Long userId);
}
