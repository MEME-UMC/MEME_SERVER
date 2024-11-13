package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r " +
            "FROM Reservation r " +
            "JOIN FETCH r.portfolio p " +
            "JOIN FETCH p.portfolioImgList pil " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE r.model = :model " +
            "AND r.status = 'COMPLETED' " +
            "ORDER BY r.createdAt desc")
    List<Reservation> findReservationByStatus(@Param(value = "model") Model model);

    @Query("SELECT r FROM Reservation r JOIN r.model m WHERE r.reservationId = :reservationId AND m.userId = :modelId")
    Optional<Reservation> findByReservationIdAndModelId(@Param("reservationId") Long reservationId, @Param("modelId") Long modelId);

}


