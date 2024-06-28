package com.wishlist.domain.exceptions;

import com.wishlist.common.exceptions.CustomException;
import com.wishlist.common.exceptions.ExceptionMetadata;
import org.springframework.http.HttpStatus;

@ExceptionMetadata(httpStatus = HttpStatus.BAD_REQUEST)
public class ItemAlreadyInTheListException extends CustomException {

    public ItemAlreadyInTheListException() {
        super(ErrorCode.ITEM_ALREADY_IN_THE_LIST.getMessage(), ErrorCode.ITEM_ALREADY_IN_THE_LIST.getCode());
    }
}
