package it.cambi.trueLayer.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ErrorResponse {

    private Date timmeStamp;
    private Integer status;
    private String error;
    private String errorMessage;
}
