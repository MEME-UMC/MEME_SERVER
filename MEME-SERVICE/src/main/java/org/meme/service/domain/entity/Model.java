package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.service.domain.enums.PersonalColor;
import org.meme.service.domain.enums.SkinType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Model {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private SkinType skinType;
    private PersonalColor personalColor;
}
