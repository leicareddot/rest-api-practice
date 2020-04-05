package com.atoz_develop.restapipractice.events;

import com.atoz_develop.restapipractice.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    @Test
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .endEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())                    // 201
                .andExpect(jsonPath("id").exists())     // 'id'가 있는지?
                .andExpect(header().exists(HttpHeaders.LOCATION))     // Location 헤더가 있는지?
                // Content-Type 헤더에 application/hal+json;charset=UTF-8가 있는지?
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))

                // 특정 필드(id 등)는 요청 파라미터로부터 입력된 값이 저장되면 안됨
                // + 비즈니스 로직 테스트
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))

                // HATEOAS 링크 응답 확인
                .andExpect(jsonPath("_links.self").exists()) // 현재 리소스 링크
                .andExpect(jsonPath("_links.query-events").exists()) // 이벤트 목록 조회 링크
                .andExpect(jsonPath("_links.update-event").exists()) // 이벤트 수정 링크
        ;
    }

    @TestDescription("입력받을수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    @Test
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)        // 요청 파라미터로부터 입력되면 안되는 필드
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .endEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)     // 요청 파라미터로부터 입력되면 안되는 필드
                .eventStatus(EventStatus.PUBLISHED) // 요청 파라미터로부터 입력되면 안되는 필드
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400
        ;
    }

    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                // 등록 종료일이 시작일보다 빠름
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 1, 1, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                // 이벤트 종료일이 시작일보다 빠름
                .endEventDateTime(LocalDateTime.of(2020, 3, 1, 12, 0))
                // basePrice가 maxPrice보다 큼
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400
                // 응답에 에러 관련 메시지 존재
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }
}
