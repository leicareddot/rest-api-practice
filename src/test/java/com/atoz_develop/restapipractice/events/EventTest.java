package com.atoz_develop.restapipractice.events;

import com.atoz_develop.restapipractice.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class EventTest {

    @TestDescription("빌더 테스트")
    @Test
    public void lombok_builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development")
                .build();
        assertThat(event).isNotNull();
    }

    @TestDescription("자바빈 규약 - 기본 생성자, getter, setter 테스트")
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

    @TestDescription("free 필드가 true인지 테스트")
    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();
    }

    @TestDescription("location 유무에 따른 offline 테스")
    @Test
    public void isOffline() {

        // Given
        Event event = Event.builder()
                .location("모임 장소")
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();
    }
}
