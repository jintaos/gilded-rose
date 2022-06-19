package com.miw.gildedrose.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {
    private  static final long serialVersionUID = 11L;

    @Schema(description = "the HTTP status code")
    private HttpStatus status;
    @Schema(description = "the error message associated with exception")
    private String message;
    @Schema(description = "timeStamp")
    private LocalDateTime timeStamp;
}
