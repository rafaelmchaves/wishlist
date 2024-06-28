package com.wishlist.adapter.output.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistJPARepository extends MongoRepository<WishlistDocument, String> {

    Optional<WishlistDocument> findByClientId(String clientId);

}
