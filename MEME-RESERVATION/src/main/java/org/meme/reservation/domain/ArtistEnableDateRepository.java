package org.meme.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistEnableDateRepository extends JpaRepository<ArtistEnableDate, Long> {
    Optional<ArtistEnableDate> findByUserId(Long userId);
}
