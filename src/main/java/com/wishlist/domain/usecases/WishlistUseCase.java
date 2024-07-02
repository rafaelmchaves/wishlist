package com.wishlist.domain.usecases;

import com.wishlist.domain.Wishlist;
import com.wishlist.domain.exceptions.WishlistNotFoundException;
import com.wishlist.domain.ports.WishlistPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class WishlistUseCase {

    private final WishlistPort wishlistPort;

    public void addProductToWishlist(String clientId, String productId) {

        var wishlist = wishlistPort.findClientWishList(clientId)
                .orElse(createNewWishlist(clientId));

        wishlist.addProductToWishList(productId);

        wishlistPort.updateWishlist(wishlist);
    }

    public Wishlist findClientWishlist(String clientId) {
        return wishlistPort.findClientWishList(clientId).orElse(null);
    }

    public boolean isProductInTheWishlist(String clientId, String productId) {
        final var wishlist = wishlistPort.findClientWishList(clientId).orElseThrow(WishlistNotFoundException::new);
        return wishlist.getProductIds().stream().anyMatch(productId::equals);
    }

    public Wishlist deleteProductFromWishlist(String clientId, String productId) {
        final var wishlist = wishlistPort.findClientWishList(clientId).orElseThrow(WishlistNotFoundException::new);
        wishlist.getProductIds().remove(productId);
        wishlistPort.updateWishlist(wishlist);

        return wishlist;
    }

    private static Wishlist createNewWishlist(String clientId) {
        final var now = LocalDateTime.now((ZoneOffset.UTC));
        return Wishlist.builder().clientId(clientId).creationDateTime(now)
                .updateDateTime(now).productIds(new ArrayList<>()).build();
    }
}
