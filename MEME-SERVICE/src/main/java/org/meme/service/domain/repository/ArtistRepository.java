package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("SELECT a " +
            "FROM Artist a " +
            "JOIN FETCH a.user u " +
            "JOIN FETCH a.portfolioList pl " +
            "WHERE a.userId = :userId")
    Optional<Artist> findArtistByUserId(@Param(value = "userId") Long userId);

}
