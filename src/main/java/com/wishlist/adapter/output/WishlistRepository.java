package com.wishlist.adapter.output;

import com.wishlist.adapter.output.mongo.WishlistDocument;
import com.wishlist.adapter.output.mongo.WishlistJPARepository;
import com.wishlist.domain.Wishlist;
import com.wishlist.domain.ports.WishlistPort;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WishlistRepository implements WishlistPort {

    private final WishlistJPARepository wishlistJPARepository;

    @Override
    public void updateWishlist(Wishlist wishlist) {
        var document = WishlistDocument.builder()
                .clientId(wishlist.getClientId())
                .productIds(wishlist.getProductIds()).build();

        if (wishlist.getId() != null) {
            document.setId(new ObjectId(wishlist.getId()));
        }

        wishlistJPARepository.save(document);
    }

    @Override
    public Optional<Wishlist> findClientWishList(String clientId) {
        final var wishlistDocument = wishlistJPARepository.findByClientId(clientId);
        return wishlistDocument.map(document -> Wishlist.builder().id(document.getId().toString()).clientId(clientId)
                .productIds(document.getProductIds()).build());
    }

}
