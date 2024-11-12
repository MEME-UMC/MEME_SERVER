package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.service.common.BaseEntity;
import org.meme.service.domain.entity.Model;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FavoriteArtist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteArtistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Model model;

    @OneToOne
    private Artist artist;

}
