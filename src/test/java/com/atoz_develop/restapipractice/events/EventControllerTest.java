package com.atoz_develop.restapipractice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

    @Test
    public void createEvent() throws Exception {

        Event event = Event.builder()
                .id(100)        // 요청 파라미터로부터 입력되면 안되는 필드
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 4, 1, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 4, 1, 12, 0))
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
                .andExpect(status().isCreated())                    // 201
                .andExpect(jsonPath("id").exists())     // 'id'가 있는지?
                .andExpect(header().exists(HttpHeaders.LOCATION))     // Location 헤더가 있는지?
                // Content-Type 헤더에 application/hal+json;charset=UTF-8가 있는지?
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))

                // 특정 필드(id 등)는 요청 파라미터로부터 입력된 값이 저장되면 안됨
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
        ;
    }
}
