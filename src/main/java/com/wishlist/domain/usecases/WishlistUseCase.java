package com.wishlist.domain.usecases;

import com.wishlist.domain.Wishlist;
import com.wishlist.domain.exceptions.ItemAlreadyInTheListException;
import com.wishlist.domain.ports.WishlistPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class WishlistUseCase {

    private static final int MAX_PRODUCTS_PER_CLIENT = 20;

    private final WishlistPort wishlistPort;

    public void addProductToWishlist(String clientId, String productId) {

        var foundWishList = wishlistPort.findClientWishList(clientId)
                .orElse(createNewWishlist(clientId));

        verifyMaxSizeLimitExceeded(foundWishList);
        isItemAlreadyInTheList(productId, foundWishList);
        foundWishList.getProductIds().add(productId);

        wishlistPort.updateWishlist(foundWishList);
    }

    public Wishlist findClientWishlist(String clientId) {
        return wishlistPort.findClientWishList(clientId).orElse(null);
    }

    public boolean isProductWishlist(String clientId, String productId) {
        final var wishlist = wishlistPort.findClientWishList(clientId).orElseThrow(RuntimeException::new);
        return wishlist.getProductIds().stream().anyMatch(productId::equals);
    }

    public Wishlist deleteProductFromWishlist(String clientId, String productId) {
        final var wishlist = wishlistPort.findClientWishList(clientId).orElseThrow(RuntimeException::new);
        wishlist.getProductIds().remove(productId);
        wishlistPort.updateWishlist(wishlist);

        return wishlist;
    }

    private static void verifyMaxSizeLimitExceeded(Wishlist foundWishList) {
        if (foundWishList.getProductIds().size() >= MAX_PRODUCTS_PER_CLIENT) {
            throw new RuntimeException("Lista de desejos pode ter no máximo 20 items.");
        }
    }

    private static void isItemAlreadyInTheList(String newProductId, Wishlist foundWishList) {
        if (foundWishList.getProductIds().stream().anyMatch(productId -> productId.equals(newProductId))) {
            throw new ItemAlreadyInTheListException();
        }
    }

    private static Wishlist createNewWishlist(String clientId) {
        return Wishlist.builder().clientId(clientId).productIds(new ArrayList<>()).build();
    }
}
