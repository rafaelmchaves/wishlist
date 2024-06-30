Feature: Remove products from Wishlist

Scenario: Delete a product from the wishlist
    Given a client with ID "9121312" and a list of products
        | 142141  |
        | 7181222 |
    When the client remove the product with ID "142141" from their wishlist
    And the client retrieves their wishlist
    Then the product with ID "142141" is not in the wishlist anymore
    And the wishlist is returned and contains
        | 7181222       |