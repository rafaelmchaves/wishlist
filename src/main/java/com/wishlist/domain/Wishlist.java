package com.wishlist.domain;

import com.wishlist.domain.exceptions.ItemAlreadyInTheListException;
import com.wishlist.domain.exceptions.LimitMaxProductsExceededException;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Wishlist {

    private static final int MAX_PRODUCTS_PER_CLIENT = 20;

    private String id;
    private List<String> productIds;
    private String clientId;

    public void addProductToWishList(String productId) {
        verifyMaxSizeLimitExceeded();
        isItemAlreadyInTheList(productId);
        productIds.add(productId);
    }

    private void verifyMaxSizeLimitExceeded() {
        if (productIds.size() >= MAX_PRODUCTS_PER_CLIENT) {
            throw new LimitMaxProductsExceededException(MAX_PRODUCTS_PER_CLIENT);
        }
    }

    private void isItemAlreadyInTheList(String newProductId) {
        if (productIds.stream().anyMatch(productId -> productId.equals(newProductId))) {
            throw new ItemAlreadyInTheListException();
        }
    }

}
