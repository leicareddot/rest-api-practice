package com.atoz_develop.restapipractice.events;

import com.atoz_develop.restapipractice.common.RestDocsConfiguration;
import com.atoz_develop.restapipractice.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@Import(RestDocsConfiguration.class)
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
                .andExpect(jsonPath("_links.profile").exists()) // profile 링크

                // REST Docs 적용
                .andDo(document("create-event", // snippet의 이름을 문자열로 지정
                        // snippets...(가변인자)
                        // 링크 문서화
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to API Documentation")
                        ),
                        // 요청 헤더 문서화
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        // 요청 필드 문서화
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Date time of begin of new event enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("Date time of close of new event enrollment"),
                                fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event")
                        ),
                        // 응답 헤더 문서화
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Url for new event"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("application/hal+json")
                        ),
                        // 응답 필드 문서화
                        responseFields(
                                fieldWithPath("id").description("Id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Date time of begin of new event enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("Date time of close of new event enrollment"),
                                fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("free").description("It tells if this event is free or not"),
                                fieldWithPath("offline").description("It tells if this event is offline or online"),
                                fieldWithPath("eventStatus").description("event status"),
                                // 링크는 상단에서 문서화 했으므로 ignore
                                fieldWithPath("_links.self.href").ignored(),
                                fieldWithPath("_links.query-events.href").ignored(),
                                fieldWithPath("_links.update-event.href").ignored(),
                                fieldWithPath("_links.profile.href").ignored()
                        )
                ))
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
