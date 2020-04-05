package com.atoz_develop.restapipractice.events;

import com.atoz_develop.restapipractice.common.TestDescription;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters(method = "parametersForTestFree")
    @Test
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 100, false}
        };
    }

    @TestDescription("location 유무에 따른 offline 테스트")
    @Parameters(method = "parametersForTestOffline")
    @Test
    public void testOffline(String location, boolean isOffline) {

        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {"    ", false}
        };
    }
}
