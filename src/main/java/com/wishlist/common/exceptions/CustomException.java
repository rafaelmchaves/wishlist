package com.wishlist.common.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final String message;
    private final String code;

    public CustomException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }

}
