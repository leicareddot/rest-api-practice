package com.atoz_develop.restapipractice.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor @Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;                                 // id
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
    private boolean offline;                            // 오프라인 여부
    private boolean free;                               // 유무료 여부
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;                    // 이벤트 상태
}
