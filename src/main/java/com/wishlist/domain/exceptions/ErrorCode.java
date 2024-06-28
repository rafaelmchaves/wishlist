package com.wishlist.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ITEM_ALREADY_IN_THE_LIST("WIS_01", "Item já existe na lista de desejos"),
    LIMIT_EXCEEDED("WIS_02", "Lista de desejos pode ter no máximo %s items."),
    WISHLIST_NOT_FOUND("WIS_03", "Lista de desejos pode ter no máximo %s items.");

    private final String code;
    private final String message;
}
