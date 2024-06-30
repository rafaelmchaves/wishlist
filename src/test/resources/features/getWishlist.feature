Feature: Get Wishlist

Scenario: Get the wishlist for a client
    Given a client with ID "9121312" and a list of products
        | 142141  |
        | 7181222 |
    When the client retrieves their wishlist
    Then the wishlist is returned and contains
        | 142141        |
        | 7181222       |

    Scenario: Get the wishlist for a client without products
    Given a client with ID "9121312" without products
    When the client retrieves their wishlist
    Then the wishlist is returned and contains
        ||

Scenario: Client doesn't have a wishlist or exist in our data base
    Given a client with ID "9121312" without wishlist
    When the client retrieves their wishlist
    Then a wishlist is not returned

Scenario: Check if a product is in the wishlist of a client
    Given a client with ID "client1" and a product with ID "product1"
    When the client checks if the product is in their wishlist
    Then an error message "A lista de desejos não foi encontrada para esse cliente." and an error code "WIL_03" is returned

Scenario: Check if a product is in the wishlist of a client, but this product is not there
    Given a client with ID "client1" and a product with ID "product1"
    When the client checks if the product is in their wishlist
    Then an error message "A lista de desejos não foi encontrada para esse cliente." and an error code "WIL_03" is returned