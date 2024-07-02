package org.meme.domain.Repository;

import org.meme.domain.Entity.Artist;
import org.meme.domain.Entity.Model;
import org.meme.domain.Entity.Portfolio;
import org.meme.domain.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.portfolio.artist = :artist")
    List<Reservation> findByArtist(@Param("artist") Artist artist);

    @Query("SELECT r FROM Reservation r WHERE r.model = :model")
    List<Reservation> findByModel(@Param("model") Model model);

    @Query("SELECT r FROM Reservation r JOIN r.model m WHERE r.reservationId = :reservationId AND m.userId = :modelId")
    Optional<Reservation> findByReservationIdAndModelId(@Param("reservationId") Long reservationId, @Param("modelId") Long modelId);

    List<Reservation> findByModelAndPortfolio(Model model, Portfolio portfolio);
}


