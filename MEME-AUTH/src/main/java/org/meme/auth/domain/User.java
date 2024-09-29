package org.meme.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.Gender;
import org.meme.domain.enums.Provider;

@AllArgsConstructor
@NoArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(length = 15)
    private String nickname;

    @NotNull
    private String profileImg;

    @NotNull
    private String role;

    @NotNull
    private boolean details;
}