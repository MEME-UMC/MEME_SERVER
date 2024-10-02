package org.meme.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Model {

    @Id
    private Long userId;

    @OneToOne
    @MapsId  // userId를 외래키로 사용하고, 동시에 Primary Key로 매핑
    @JoinColumn(name = "user_id")
    private User user;

    // Details
    private SkinType skinType;
    private PersonalColor personalColor;
}
