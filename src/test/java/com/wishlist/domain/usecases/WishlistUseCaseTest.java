package com.wishlist.domain.usecases;

import com.wishlist.domain.Wishlist;
import com.wishlist.domain.ports.WishlistPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WishlistUseCaseTest {

    @InjectMocks
    private WishlistUseCase wishlistUseCase;

    @Mock
    private WishlistPort wishlistPort;

    @Test
    public void addProductToWishlist_givenClientIdAndProductId_productWasAddedToWishlistSuccessfully() {

        final String productId = "3441221";
        final String clientId = "2141412421";
        final String wishlistId = "12421421";
        final var wishlist = Wishlist.builder().id(wishlistId).clientId(clientId)
                .productIds(new ArrayList<>())
                .build();

        Mockito.when(wishlistPort.findClientWishList(clientId)).thenReturn(Optional.of(wishlist));

        wishlistUseCase.addProductToWishlist(clientId, productId);

        final var argumentCaptor = ArgumentCaptor.forClass(Wishlist.class);
        Mockito.verify(wishlistPort, Mockito.times(1)).updateWishlist(argumentCaptor.capture());

        final var wishlistCaptured = argumentCaptor.getValue();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(clientId, wishlistCaptured.getClientId());
            Assertions.assertEquals(1, wishlistCaptured.getProductIds().size());
            Assertions.assertEquals(productId, wishlistCaptured.getProductIds().get(0));
            Assertions.assertEquals(wishlistId, wishlistCaptured.getId());
        });

    }

    @Test
    public void addProductToWishlist_givenClientIdAndProductIdAndThereIsNoWishlist_productWasAddedToWishlistSuccessfully() {

        final String productId = "3441221";
        final String clientId = "2141412421";

        Mockito.when(wishlistPort.findClientWishList(clientId)).thenReturn(Optional.empty());

        wishlistUseCase.addProductToWishlist(clientId, productId);

        final var argumentCaptor = ArgumentCaptor.forClass(Wishlist.class);
        Mockito.verify(wishlistPort, Mockito.times(1)).updateWishlist(argumentCaptor.capture());

        final var wishlistCaptured = argumentCaptor.getValue();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(clientId, wishlistCaptured.getClientId());
            Assertions.assertEquals(1, wishlistCaptured.getProductIds().size());
            Assertions.assertEquals(productId, wishlistCaptured.getProductIds().get(0));
        });

    }
}
