package org.meme.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(length = 100)
    private String username;

    @NotNull
    @Column(length = 40)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Provider provider;

    @NotNull
    private String password;

    @NotNull
    @Column(length = 15)
    private String nickname;

    @NotNull
    private String profileImg;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private boolean details;  // 세부 정보 입력 여부
}