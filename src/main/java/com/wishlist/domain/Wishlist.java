package com.wishlist.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Wishlist {

    private String id;
    private List<String> productIds;
    private String clientId;

}
