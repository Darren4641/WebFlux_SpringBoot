package com.webflux.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String message;
    private T data;

    public APIResponse(T data) {
        this.code = Code.OK.getCode();
        this.message = Code.OK.getMessage();
        this.data = data;
    }

    public APIResponse(Code code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}
