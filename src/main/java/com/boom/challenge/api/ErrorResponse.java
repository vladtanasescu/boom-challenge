package com.boom.challenge.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    String message;
}
