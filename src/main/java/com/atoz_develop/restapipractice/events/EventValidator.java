package com.atoz_develop.restapipractice.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    // 비즈니스 로직 위배 사항 분기
    public void validate(EventDto eventDto, Errors errors) {
        // maxPrice가 0인 경우에만 basePrice가 maxPrice보다 클 수 있음(경매 형태)
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            // Field Error
//            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong.");
//            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong.");

            // Global Error
            errors.reject("wrongPrices", "Value of Prices are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
        }

        // TODO BeginEventDateTime 검증
        // TODO CloseEnrollmentDateTime 검증
    }
}
