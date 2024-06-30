Feature: Remove products from Wishlist

Scenario: Delete a product from the wishlist
    Given a client with ID "client1" and two products with ID "142141" and "7181222"
    When the client deletes the product with ID "142141" from their wishlist
    Then the product with ID "142141" the wishlist
    And the wishlist contains "7181222"