package com.saklecha.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DSError {

    private String code;
    private Object description;
    private String timestamp;

    public DSError(String code, Object description) {
        this.code = code;
        this.description = description;
    }

    public DSError ( String code ) {
        this.code = code;
    }


}
