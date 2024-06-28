package com.wishlist.domain.ports;

import com.wishlist.domain.Wishlist;

import java.util.Optional;


public interface WishlistPort {
    void updateWishlist(Wishlist wishlist);

    Optional<Wishlist> findClientWishList(String clientId);

}
