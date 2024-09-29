package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT p FROM Portfolio p " +
            "WHERE p.artist = :artist " +
            "AND p.isBlock = false")
    Page<Portfolio> findByArtist(@Param("artist") Artist artist, Pageable pageable);

    @Query("SELECT p FROM Portfolio p " +
            "WHERE p.category = :category " +
            "AND p.isBlock = false ")
    Page<Portfolio> findByCategory(@Param("category") Category category, Pageable pageable);

    @Query("SELECT p FROM Portfolio p " +
            "WHERE (p.makeupName LIKE %:query% OR p.info LIKE %:query%) " +
            "AND p.isBlock = false" )
    Page<Portfolio> search(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Portfolio p WHERE p.isBlock = false")
    Page<Portfolio> findAllNotBlocked(Pageable pageable);

    boolean existsByMakeupName(String makeupName);

}
