package com.wishlist.adapter.output;

import com.wishlist.adapter.output.mongo.WishlistDocument;
import com.wishlist.adapter.output.mongo.WishlistJPARepository;
import com.wishlist.domain.Wishlist;
import com.wishlist.domain.ports.WishlistPort;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WishlistRepository implements WishlistPort {

    private final WishlistJPARepository wishlistJPARepository;

    @Override
    @CacheEvict(value = "wishlist", key = "#wishlist.clientId")
    public void updateWishlist(Wishlist wishlist) {
        var document = WishlistDocument.builder()
                .clientId(wishlist.getClientId())
                .creationDateTime(wishlist.getCreationDateTime())
                .updateDateTime(wishlist.getUpdateDateTime())
                .productIds(wishlist.getProductIds()).build();

        if (wishlist.getId() != null) {
            document.setId(new ObjectId(wishlist.getId()));
            document.setUpdateDateTime(LocalDateTime.now(ZoneOffset.UTC));
        }

        wishlistJPARepository.save(document);
    }

    @Override
    @Cacheable(value = "wishlist", key = "#clientId")
    public Optional<Wishlist> findClientWishList(String clientId) {
        final var wishlistDocument = wishlistJPARepository.findByClientId(clientId);
        return wishlistDocument.map(document -> Wishlist.builder().id(document.getId().toString()).clientId(clientId)
                .creationDateTime(document.getCreationDateTime()).updateDateTime(document.getUpdateDateTime())
                .productIds(document.getProductIds()).build());
    }

}
