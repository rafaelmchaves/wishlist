package com.wishlist.adapter.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WishlistResponse {

    private String clientId;
    private List<String> productIds;

}
