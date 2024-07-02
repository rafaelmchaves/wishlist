package com.wishlist.adapter.input.controller;

import com.wishlist.adapter.input.controller.response.WishlistResponse;
import com.wishlist.common.exceptions.ErrorMessage;
import com.wishlist.domain.Wishlist;
import com.wishlist.domain.usecases.WishlistUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Wishlist", description = "Wishlist API's")
public class WishlistController {

    private final WishlistUseCase wishlistUseCase;

    @PostMapping("/clients/{clientId}/wishlist/products/{productId}")
    @Operation(summary = "Adicione um produto à lista de desejos do cliente. Se o cliente ainda não tiver uma lista de desejos, criamos a estrutura da lista de desejos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wishlist criada"),
            @ApiResponse(responseCode = "400", description = "Limite máximo de produtos na lista de desejos alcançados \n" +
                    "Item já foi adicionado na lista de desejos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    public ResponseEntity<Void> addProductToWishList(@PathVariable String clientId, @PathVariable String productId) {
        wishlistUseCase.addProductToWishlist(clientId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Buscar todos os produtos da lista de desejo do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de desejos do cliente foi retornada com sucesso")
    })
    @GetMapping("/clients/{clientId}/wishlists")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String clientId) {
        final var wishlist = wishlistUseCase.findClientWishlist(clientId);
        return wishlist != null ? ResponseEntity.ok(getWishlistResponse(wishlist))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/{clientId}/wishlist/products/{productId}")
    @Operation(summary = "Verifica se o cliente tem um produto específico na lista de desejos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto foi encontrado dentro da lista de desejo e retornado no corpo da resposta"),
            @ApiResponse(responseCode = "204", description = "Produto não foi encontrado na lista de desejos do cliente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "Wishlist não foi encontrada para aquele cliente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    public ResponseEntity<WishlistResponse> isProductWishlist(@PathVariable String clientId, @PathVariable String productId) {
        final boolean isProductWishlist = wishlistUseCase.isProductInTheWishlist(clientId, productId);
        return isProductWishlist ? ResponseEntity.ok(WishlistResponse.builder().clientId(clientId).productIds(List.of(productId)).build())
                : ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um produto específico na lista de desejos do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto foi excluído com sucesso da lista de desejos"),
            @ApiResponse(responseCode = "404", description = "Wishlist não foi encontrada para o cliente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/clients/{clientId}/wishlist/products/{productId}")
    public ResponseEntity<WishlistResponse> deleteProductFromWishlist(@PathVariable String clientId, @PathVariable String productId) {
        final var wishlist = wishlistUseCase.deleteProductFromWishlist(clientId, productId);
        return ResponseEntity.ok(WishlistResponse.builder().clientId(wishlist.getClientId()).productIds(wishlist.getProductIds()).build());
    }

    private static WishlistResponse getWishlistResponse(Wishlist wishlist) {
        return WishlistResponse.builder().clientId(wishlist.getClientId()).productIds(wishlist.getProductIds())
                .creationDateTime(wishlist.getCreationDateTime()).updateDateTime(wishlist.getUpdateDateTime())
                .build();
    }

}
