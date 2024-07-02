package org.meme.domain.Repository;

import org.meme.domain.Entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
