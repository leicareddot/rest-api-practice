package com.atoz_develop.restapipractice.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class EventTest {

    // 빌더 테스트
    @Test
    public void lombok_builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development")
                .build();
        assertThat(event).isNotNull();
    }

    // 자바빈 규약 - 기본 생성자, getter, setter 테스트
    @Test
    public void lombok_javaBean() {
        String name = "Spring Event";
        String description = "Event!!!";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}
