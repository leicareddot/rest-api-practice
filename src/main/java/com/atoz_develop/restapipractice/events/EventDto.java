package com.atoz_develop.restapipractice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;                                // 이름

    @NotEmpty
    private String description;                         // 설명

    @NotNull
    private LocalDateTime beginEnrollmentDateTime;      // 등록 시작일

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;      // 등록 종료일

    @NotNull
    private LocalDateTime beginEventDateTime;           // 이벤트 시작일

    @NotNull
    private LocalDateTime endEventDateTime;             // 이벤트 종료

    private String location;                            // (optional) 이게 없으면 온라인 모임

    @Min(0)
    private int basePrice;                              // (optional)

    @Min(0)
    private int maxPrice;                               // (optional)

    @Min(0)
    private int limitOfEnrollment;                      // 등록 정원
}
