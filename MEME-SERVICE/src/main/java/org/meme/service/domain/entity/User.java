package org.meme.service.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.meme.service.domain.entity.Inquiry;
import org.meme.service.domain.enums.Gender;
import org.meme.service.domain.enums.Provider;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.JOINED)
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
    private boolean details;

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
//    @Builder.Default
    private Set<String> deviceTokens = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    protected List<Inquiry> inquiryList;

    public boolean getDetails() {
        return details;
    }

    public void updateInquiryList(Inquiry inquiry){
        this.inquiryList.add(inquiry);
    }

    public void updateProfileImg(String profileimg){
        this.profileImg = profileimg;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateGender(Gender gender){
        if (gender != null)
            this.gender = gender;
    }
}
