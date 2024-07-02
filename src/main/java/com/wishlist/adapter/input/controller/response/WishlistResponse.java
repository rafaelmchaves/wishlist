package com.wishlist.adapter.input.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class WishlistResponse {

    private String clientId;
    private List<String> productIds;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;

}
