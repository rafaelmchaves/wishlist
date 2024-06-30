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
import java.util.List;
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

    @Test
    void findClientWishlist_clientIdExist_returnWishlist() {
        final String clientId = "2141412421";
        final String id = "73453252";

        Mockito.when(wishlistPort.findClientWishList(clientId)).thenReturn(Optional.of(Wishlist.builder().clientId(clientId)
                .id(id).build()));

        final var wishlist = wishlistUseCase.findClientWishlist(clientId);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(clientId, wishlist.getClientId());
            Assertions.assertEquals(id, wishlist.getId());
        });
    }

    @Test
    void findClientWishlist_noWishListFound_returnNull() {
        final String clientId = "2141412421";

        Mockito.when(wishlistPort.findClientWishList(clientId)).thenReturn(Optional.empty());

        final var wishlist = wishlistUseCase.findClientWishlist(clientId);

       Assertions.assertNull(wishlist);
    }

    @Test
    void deleteProductFromWishlist_listWith2Products_oneProductIsRemoved() {
        final String clientId = "2141412421";

        final String product1 = "1241241";
        final String product2 = "489532";
        List<String> products = new ArrayList<String>();
        products.add(product1);
        products.add(product2);

        final var wishlist = Wishlist.builder().id("342423").clientId(clientId)
                .productIds(products)
                .build();
        Mockito.when(wishlistPort.findClientWishList(clientId)).thenReturn(Optional.of(wishlist));

        wishlistUseCase.deleteProductFromWishlist(clientId, product1);

        final var wishlistArgumentCaptor = ArgumentCaptor.forClass(Wishlist.class);
        Mockito.verify(wishlistPort, Mockito.times(1)).updateWishlist(wishlistArgumentCaptor.capture());
        final var capturedWishlist = wishlistArgumentCaptor.getValue();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(1, capturedWishlist.getProductIds().size());
            Assertions.assertEquals(clientId, capturedWishlist.getClientId());
            Assertions.assertEquals(product2, capturedWishlist.getProductIds().get(0));
        });

    }

}
