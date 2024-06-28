package com.wishlist.adapter.controller;

import com.wishlist.adapter.controller.requests.WishlistRequest;
import com.wishlist.adapter.controller.response.WishlistResponse;
import com.wishlist.domain.Wishlist;
import com.wishlist.domain.usecases.WishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WishlistController {

    private final WishlistUseCase wishlistUseCase;
    @PostMapping("/wishlists")
    public ResponseEntity<Void> addProductToWishList(@RequestBody WishlistRequest wishlistRequest) {
        wishlistUseCase.addProductToWishlist(wishlistRequest.getClientId(), wishlistRequest.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/clients/{clientId}/wishlists")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String clientId) {
        final var wishlist = wishlistUseCase.findClientWishlist(clientId);

        return wishlist != null ? ResponseEntity.ok(getWishlistResponse(wishlist))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/{clientId}/wishlists/products/{productId}")
    public ResponseEntity<WishlistResponse> isProductWishlist(@PathVariable String clientId, @PathVariable String productId) {
        final boolean isProductWishlist = wishlistUseCase.isProductWishlist(clientId, productId);

        return isProductWishlist ? ResponseEntity.ok(WishlistResponse.builder().clientId(clientId).productIds(List.of(productId)).build())
                : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clients/{clientId}/wishlist/products/{productId}")
    public ResponseEntity<WishlistResponse> deleteProductFromWishlist(@PathVariable String clientId, @PathVariable String productId) {
        final var wishlist = wishlistUseCase.deleteProductFromWishlist(clientId, productId);
        return ResponseEntity.ok(WishlistResponse.builder().clientId(wishlist.getClientId()).productIds(wishlist.getProductIds()).build());
    }

    private static WishlistResponse getWishlistResponse(Wishlist wishlist) {
        return WishlistResponse.builder().clientId(wishlist.getClientId()).productIds(wishlist.getProductIds()).build();
    }

}
