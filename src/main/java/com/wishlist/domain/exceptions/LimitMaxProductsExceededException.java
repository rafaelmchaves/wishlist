package com.wishlist.domain.exceptions;

import com.wishlist.common.exceptions.CustomException;
import com.wishlist.common.exceptions.ExceptionMetadata;
import org.springframework.http.HttpStatus;

@ExceptionMetadata(httpStatus = HttpStatus.BAD_REQUEST)
public class LimitMaxProductsExceededException extends CustomException {
    public LimitMaxProductsExceededException(int maxSize) {
        super(String.format(ErrorCode.LIMIT_EXCEEDED.getMessage(), maxSize), ErrorCode.LIMIT_EXCEEDED.getCode());
    }
}
