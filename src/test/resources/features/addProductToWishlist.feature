Feature: Add products to Wishlist

  Scenario: Add the first product in the wishlist
    Given a client with ID "128981341" and a product with ID "18917311"
    When the client adds the product to their wishlist
    Then the product is added to the wishlist successfully
    And the product with ID "18917311" was added to the wishlist successfully

  Scenario: Add a second product in the wishlist
    Given a client with ID "128981341" and a product with ID "18917311"
    And the product with ID "91371782" already exists in the wishlist
    When the client adds the product to their wishlist
    Then the product is added to the wishlist successfully
    And the product with ID "91371782" was added to the wishlist successfully
    And  the product with ID "18917311" was added to the wishlist successfully

  Scenario: Try to add the same product in the wishlist
    Given a client with ID "128981341" and a product with ID "18917311"
    And the product with ID "18917311" already exists in the wishlist
    When the client adds the product to their wishlist
    Then the product is not added to the wishlist
    And an error message "Item já existe na lista de desejos" and an error code "WIL_01" is returned

  Scenario: Adicionar o primeiro produto na wishlist do cliente com sucesso
    Given a client with ID "128981341" and a product with ID "18917311"
    And the wishlist contains "20" products
    When the client adds the product to their wishlist
    Then the product is not added to the wishlist
    And an error message "Lista de desejos pode ter no máximo 20 items." and an error code "WIL_02" is returned

