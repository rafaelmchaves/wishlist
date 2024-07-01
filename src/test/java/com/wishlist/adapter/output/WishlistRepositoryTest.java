package com.wishlist.adapter.output;

import com.wishlist.adapter.output.mongo.WishlistDocument;
import com.wishlist.adapter.output.mongo.WishlistJPARepository;
import com.wishlist.domain.Wishlist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class WishlistRepositoryTest {

    @InjectMocks
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistJPARepository wishlistJPARepository;

    @Test
    void updateWishlist_saveWishListForTheFirstTime_saveCorrectly() {
        final var clientId = "341412";
        final var productId = "93293y2";
        List<String> list = new ArrayList<>();
        list.add(productId);
        final var wishlist = Wishlist.builder().clientId(clientId).productIds(list).build();

        wishlistRepository.updateWishlist(wishlist);

        final var argumentCaptorWishlistDocument = ArgumentCaptor.forClass(WishlistDocument.class);
        Mockito.verify(wishlistJPARepository, Mockito.times(1)).save(argumentCaptorWishlistDocument.capture());

        final var document = argumentCaptorWishlistDocument.getValue();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(clientId, document.getClientId());
            Assertions.assertEquals(productId, document.getProductIds().get(0));
        });
    }

    @Test
    void updateWishlist_updateProductsInTheWishList_saveCorrectly() {
        final var clientId = "341412";
        final var productId = "93293y2";
        final var id = "6683378553cf8d3aa0ab7463";

        List<String> list = new ArrayList<>();
        list.add(productId);
        final var wishlist = Wishlist.builder().id(id).clientId(clientId).productIds(list).build();

        wishlistRepository.updateWishlist(wishlist);

        final var argumentCaptorWishlistDocument = ArgumentCaptor.forClass(WishlistDocument.class);
        Mockito.verify(wishlistJPARepository, Mockito.times(1)).save(argumentCaptorWishlistDocument.capture());

        final var document = argumentCaptorWishlistDocument.getValue();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(clientId, document.getClientId());
            Assertions.assertEquals(id, document.getId().toString());
            Assertions.assertEquals(productId, document.getProductIds().get(0));
        });
    }

}
