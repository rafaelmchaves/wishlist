package com.wishlist.domain.exceptions;

import com.wishlist.common.exceptions.CustomException;
import com.wishlist.common.exceptions.ExceptionMetadata;
import org.springframework.http.HttpStatus;

@ExceptionMetadata(httpStatus = HttpStatus.NOT_FOUND)
public class WishlistNotFoundException extends CustomException {

    public WishlistNotFoundException() {
        super(ErrorCode.WISHLIST_NOT_FOUND.getCode(), ErrorCode.WISHLIST_NOT_FOUND.getMessage());
    }
}
