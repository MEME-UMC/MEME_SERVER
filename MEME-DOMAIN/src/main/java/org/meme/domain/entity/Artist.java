package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.meme.domain.enums.*;

import java.util.List;

@SuperBuilder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Artist extends User {

    @Column(length = 500, nullable = true)
    private String introduction;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private WorkExperience workExperience;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private List<Region> region;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private List<Category> specialization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private MakeupLocation makeupLocation;

    @Column(nullable = true)
    private String shopLocation; // 샵의 위치

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artist")
    private List<Portfolio> portfolioList;

    public void updatePortfolioList(Portfolio portfolio){
        this.portfolioList.add(portfolio);
    }


//    public void tempMethod(){
//        this.username = "name";
//        this.email="";
//        this.password="";
//        this.workExperience=WorkExperience.FIVE;
//        this.role="ARTIST";
//        this.userStatus = UserStatus.ACTIVE;
//        this.provider = Provider.KAKAO;
//        this.details = false;
//    }
}
