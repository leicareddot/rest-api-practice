package com.atoz_develop.restapipractice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    private String name;                                // 이름
    private String description;                         // 설명
    private LocalDateTime beginEnrollmentDateTime;      // 등록 시작일
    private LocalDateTime closeEnrollmentDateTime;      // 등록 종료일
    private LocalDateTime beginEventDateTime;           // 이벤트 시작일
    private LocalDateTime endEventDateTime;             // 이벤트 종료
    private String location;                            // (optional) 이게 없으면 온라인 모임
    private int basePrice;                              // (optional)
    private int maxPrice;                               // (optional)
    private int limitOfEnrollment;                      // 등록 정원
}
