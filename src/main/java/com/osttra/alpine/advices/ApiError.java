package com.osttra.alpine.advices;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private LocalDateTime timeStamp;
    private HttpStatus httpStatus;
    private String error;
}
