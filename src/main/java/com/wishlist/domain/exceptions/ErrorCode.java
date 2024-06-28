package com.wishlist.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ITEM_ALREADY_IN_THE_LIST("ACC_01", "Item já existe na lista de desejos");

    private final String code;
    private final String message;
}
