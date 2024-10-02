package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.enums.Category;
import org.meme.service.domain.enums.MakeupLocation;
import org.meme.service.domain.enums.Region;
import org.meme.service.domain.enums.WorkExperience;

import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Artist extends User{

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
}