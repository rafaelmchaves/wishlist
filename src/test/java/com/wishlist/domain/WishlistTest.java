package com.wishlist.domain;

import com.wishlist.domain.exceptions.ItemAlreadyInTheListException;
import com.wishlist.domain.exceptions.LimitMaxProductsExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistTest {

    @DisplayName("GIVEN a client id and a new product id to be added to the wishlist " +
            "and the list of the products in the wishlist are empty " +
            "WHEN I add the product to the wishlist " +
            "THEN the wishlist contains 1 product " +
            "AND the product was added in the wishlist ")
    @Test
    void addProductToWishList_productsEmptyAndAddANewProduct_productAddedSuccessfully() {
        //GIVEN a client id and a new product id to be added to the wishlist
        String clientId = "34141224";
        String productId = "17826841";
        //AND the list of the products in the wishlist are empty
        final var wishlist = Wishlist.builder().clientId(clientId).productIds(new ArrayList<>()).build();

        //WHEN I add the product to the wishlist
        wishlist.addProductToWishList(productId);

        //THEN the wishlist contains 1 product
        //AND the product was added in the wishlist
        assertAll(() -> {
            assertEquals(1, wishlist.getProductIds().size());
            assertEquals(productId, wishlist.getProductIds().get(0));
        });

    }

    @DisplayName("GIVEN a client id and a new product id to be added to the wishlist " +
            "and there is one product in the wishlist" +
            "WHEN I add the product to the wishlist " +
            "THEN the wishlist contains 2 products " +
            "AND the product was added in the wishlist " +
            "AND the old product also is in the list")
    @Test
    void addProductToWishList_OneProductInTheListAndAddANewProduct_productAddedSuccessfully() {
        //GIVEN a client id and a new product id to be added to the wishlist
        String clientId = "34141224";
        String oldProduct = "1241242421";
        String newProductId = "17826841";
        //AND there is one product in the wishlist
        final var products = new ArrayList<String>();
        products.add(oldProduct);
        final var wishlist = Wishlist.builder().clientId(clientId).productIds(products).build();

        //WHEN I add the product to the wishlist
        wishlist.addProductToWishList(newProductId);


        //THEN the wishlist contains 2 products
        //AND the new product was added in the wishlist
        //AND the old product also is in the list
        assertAll(() -> {
            assertEquals(2, wishlist.getProductIds().size());
            assertEquals(oldProduct, wishlist.getProductIds().get(0));
            assertEquals(newProductId, wishlist.getProductIds().get(1));
        });

    }

    @DisplayName("GIVEN a client id and a new product id to be added to the wishlist " +
            "AND there are 20 products in the wishlist " +
            "WHEN I add the product to the wishlist " +
            "THEN an exception to limit max products exceeded is thrown " +
            "AND the product is not added in the list ")
    @Test
    void addProductToWishList_TwentyProductsInTheListAndAddANewProduct_LimitExceededExceptionIsThrown() {
        //GIVEN a client id and a new product id to be added to the wishlist
        String clientId = "34141224";
        String newProductId = "17826841";

        //AND there are 20 products in the wishlist
        final var products = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            products.add(UUID.randomUUID().toString());
        }
        final var wishlist = Wishlist.builder().clientId(clientId).productIds(products).build();

        //WHEN I add the product to the wishlist
        //THEN an exception to limit max products exceeded is thrown
        assertThrows(LimitMaxProductsExceededException.class, ()-> wishlist.addProductToWishList(newProductId));

        //AND the product is not added in the list
        assertAll(() -> {
            assertEquals(20, wishlist.getProductIds().size());
        });

    }

    @DisplayName("GIVEN a client id and a new product id to be added to the wishlist " +
            "AND there are 20 products in the wishlist " +
            "WHEN I try to add the product again " +
            "THEN an exception is thrown " +
            "AND the wishlist has 1 element" +
            "AND it contains the product id ")
    @Test
    void addProductToWishList_addTheSameProductInTheList_itemAlreadyInTheListExceptionIsThrown() {
        //GIVEN a client id and a new product id to be added to the wishlist
        String clientId = "34141224";
        String productId = "17826841";

        //AND the productId is in the wishlist
        final var products = new ArrayList<String>();
        products.add(productId);
        final var wishlist = Wishlist.builder().clientId(clientId).productIds(products).build();

        //WHEN I try to add the product again
        //THEN an exception is thrown
        assertThrows(ItemAlreadyInTheListException.class, () -> wishlist.addProductToWishList(productId));

        //AND the wishlist has 1 element
        //AND it contains the product id
        assertAll(() -> {
            assertEquals(1, wishlist.getProductIds().size());
            assertEquals(productId, wishlist.getProductIds().get(0));
        });

    }
}
