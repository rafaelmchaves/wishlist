package com.wishlist.adapter.output.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Document("wishlists")
public class WishlistDocument {

    @Id
    @Setter
    private ObjectId id;

    @Indexed(unique = true)
    private String clientId;
    private List<String> productIds;
    private LocalDateTime creationDateTime;

    @Setter
    private LocalDateTime updateDateTime;

}
