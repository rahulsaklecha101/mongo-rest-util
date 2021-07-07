package com.saklecha.response;

import com.saklecha.model.DSError;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    private DSError error;

    public BaseResponse(String errorCode, String errorMessage) {
        this.error = new DSError(errorCode, errorMessage);
    }

    public DSError getError(){
        return this.error;
    }

}
