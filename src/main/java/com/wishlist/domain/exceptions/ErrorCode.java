package com.wishlist.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ITEM_ALREADY_IN_THE_LIST("WIL_01", "Item já existe na lista de desejos"),
    LIMIT_EXCEEDED("WIL_02", "Lista de desejos pode ter no máximo %s items."),
    WISHLIST_NOT_FOUND("WIL_03", "A lista de desejos não foi encontrada para esse cliente.");

    private final String code;
    private final String message;
}
