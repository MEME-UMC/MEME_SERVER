package org.meme.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meme.domain.enums.Gender;
import org.meme.domain.enums.Provider;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@SuperBuilder @Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    protected String profileImg;

    @NotNull
    @Column(length = 15)
    protected String nickname;

    @NotNull
    @Column(unique = true, length = 100)
    private String username;

    @NotNull
    @Column(length = 40)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String role;

    @NotNull
    private boolean details;  // 새롭게 추가, 기본 값은 false

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    protected Gender gender;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Provider provider;

    @ElementCollection
    @Builder.Default
    private Set<String> deviceTokens = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    protected List<Inquiry> inquiryList;
}
