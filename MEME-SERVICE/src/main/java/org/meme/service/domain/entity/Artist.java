package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.service.domain.enums.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Artist {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500, nullable = true)
    private String introduction;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true)
    private String instagramId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private WorkExperience workExperience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private MakeupLocation makeupLocation;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private List<Region> region;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private List<Category> specialization;

    @Column(nullable = true)
    private String shopLocation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artist")
    private List<Portfolio> portfolioList;

    public void updatePortfolioList(Portfolio portfolio){
        this.portfolioList.add(portfolio);
    }

    public void updateIntroduction(String introduction) {
        if (!introduction.isEmpty()) {
            this.introduction = introduction;
        }
    }

    public void updateWorkExperience(WorkExperience workExperience) {
        if (workExperience != null) {
            this.workExperience = workExperience;
        }
    }

    public void updateRegion(List<Region> region) {
        if (!region.isEmpty()) {
            this.region = region;
        }
    }

    public void updateSpecialization(List<Category> specialization) {
        if (!specialization.isEmpty()) {
            this.specialization = specialization;
        }
    }

    public void updateMakeupLocation(MakeupLocation makeupLocation) {
        if (makeupLocation != null) {
            this.makeupLocation = makeupLocation;
        }
    }

    public void updateShopLocation(String shopLocation) {
        if (!shopLocation.isEmpty()) {
            this.shopLocation = shopLocation;
        }
    }

}