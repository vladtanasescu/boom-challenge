package com.boom.challenge.service.validation;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OrderDateValidatorService {
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(20, 0);

    public boolean isValid(LocalDateTime localDateTime) {
        LocalTime requestedTime = localDateTime.toLocalTime();
        return isInFuture(localDateTime) && isTimeInAllowedInterval(requestedTime);
    }

    private boolean isInFuture(LocalDateTime requestedTime) {
        return LocalDateTime.now().isBefore(requestedTime);
    }

    private boolean isTimeInAllowedInterval(LocalTime requestedTime) {
        return requestedTime.isAfter(START_TIME) && requestedTime.isBefore(END_TIME);
    }
}
