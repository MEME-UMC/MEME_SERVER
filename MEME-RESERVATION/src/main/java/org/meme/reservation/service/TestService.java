package org.meme.reservation.service;

import lombok.RequiredArgsConstructor;
import org.meme.domain.entity.*;
import org.meme.domain.enums.*;
import org.meme.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TestService {

    private final ArtistRepository artistRepository;
    private final ModelRepository modelRepository;
    private final PortfolioRepository portfolioRepository;
    private final ArtistEnableDateRepository enableDateRepository;
    private final ArtistEnableTimeRepository enableTimeRepository;

    @Transactional
    public void readyForService() {
        // 1. 아티스트 회원가입
        Artist artistSunwoo = Artist.builder()
                .username("박선우")
                .nickname("경희대 서강준")
                .profileImg("image-seokangjun")
                .email("seokangjun@kakao.com")
                .password("seokangjun@kakao.com")
                .role("ARTIST")
                .details(false)
                .gender(Gender.MALE)
                .provider(Provider.KAKAO)
                .introduction("안녕하세요. 서강준입니다.")
                .phoneNumber("010-6379-8080")
                .instagramId("@seokangjun")
                .build();
        artistRepository.save(artistSunwoo);

        // 2. 포트폴리오 등록
        Portfolio portfolio = Portfolio.builder()
                .artist(artistSunwoo)
                .category(Category.PARTY)
                .makeupName("테라폼 메이크업")
                .price(10000)
                .info("메이크업도 IaaC의 시대")
                .durationTime("1시간 30분")
                .build();
        portfolioRepository.save(portfolio);

        // 3. 가능 날짜 및 시간대 등록
        ArtistEnableDate enableDate = ArtistEnableDate.builder()
                .artist(artistSunwoo)
                .year(2024)
                .month(7)
                .enableDates("2024-07-01,2024-07-02,2024-07-03,2024-07-04,2024-07-05,2024-07-06,2024-07-07,2024-07-08,2024-07-09,2024-07-12,2024-07-15,2024-07-16,2024-07-17,2024-07-18,2024-07-23,2024-07-24,2024-07-26,2024-07-30")
                .build();

        ArtistEnableTime enableTime = ArtistEnableTime.builder()
                .artist(artistSunwoo)
                .enableTimes(
                        "MON_06:00,06:30,07:00,07:30,08:00,08:30,09:00,09:30,10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30" + ";" +
                                "TUE_07:00,07:30,08:00,08:30,09:00,09:30,10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30,18:00,18:30" + ";" +
                                "WED_08:00,08:30,09:00,09:30,10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30,18:00" + ";" +
                                "THU_06:00,06:30,07:00,07:30,08:00,08:30,09:00,09:30,10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30" + ";" +
                                "FRI_06:00,06:30,07:00,07:30,08:00,08:30,09:00,09:30,10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30" + ";" +
                                "SAT_13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00" + ";" +
                                "SUN_10:00,10:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30,18:00"
                )
                .build();

        enableDateRepository.save(enableDate);
        enableTimeRepository.save(enableTime);

        // 4. 모델 회원가입
        Model modelJaeyoung = Model.builder()
                .username("임재영")
                .nickname("상명대 송강")
                .profileImg("image-songkang")
                .email("songkang@kakao.com")
                .password("songkang@kakao.com")
                .role("MODEL")
                .details(false)
                .gender(Gender.MALE)
                .provider(Provider.KAKAO)
                .skinType(SkinType.DRY)
                .personalColor(PersonalColor.AUTUMN)
                .build();

        Model modelDaeun = Model.builder()
                .username("김다은")
                .nickname("숭실대 안유진")
                .profileImg("image-anyujin")
                .email("anyujin@kakao.com")
                .password("anyujin@kakao.com")
                .role("MODEL")
                .details(false)
                .gender(Gender.FEMALE)
                .provider(Provider.KAKAO)
                .skinType(SkinType.OILY)
                .personalColor(PersonalColor.WINTER)
                .build();

        modelRepository.save(modelJaeyoung);
        modelRepository.save(modelDaeun);
    }
}
