package it.cambi.trueLayer.exception.model;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {

    private Date timmeStamp;
    private Integer status;
    private String error;
    private String errorMessage;
}
