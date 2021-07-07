package com.saklecha.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BOGenericResponse extends BaseResponse {

    private Object data;
    private String message;

    public BOGenericResponse() {}

    public BOGenericResponse(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
