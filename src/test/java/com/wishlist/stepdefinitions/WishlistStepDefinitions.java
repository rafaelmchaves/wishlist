package com.wishlist.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishlist.adapter.input.controller.response.WishlistResponse;
import com.wishlist.adapter.output.mongo.WishlistDocument;
import com.wishlist.adapter.output.mongo.WishlistJPARepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "tests")
@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WishlistStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WishlistJPARepository wishlistJPARepository;

    private ResultActions resultActions;

    private String productId;
    private String clientId;

    private WishlistDocument resultedWishlist;

    @Autowired
    private CacheManager cacheManager;

    @Given("a client with ID {string} and a product with ID {string}")
    public void aClientWithIDAndAProductWithID(String clientId, String productId) {
        this.clientId = clientId;
        this.productId = productId;
    }

    @Given("the product with ID {string} already exists in the wishlist")
    public void theProductWithIDAlreadyExistsInTheWishlist(String productId) {
        final var document = WishlistDocument.builder().clientId(clientId).productIds(new ArrayList<>()).build();
        document.getProductIds().add(productId);
        wishlistJPARepository.save(document);
    }

    @Given("a client with ID {string} without wishlist")
    public void aClientWithIDWithoutWishlist(String clientId) {
        this.clientId = clientId;
    }

    @Given("the wishlist contains {string} products")
    public void theWishlistContainsProducts(String amount) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(amount); i++) {
            list.add(UUID.randomUUID().toString());
        }
        WishlistDocument wishlistDocument = WishlistDocument.builder().clientId(clientId)
                .productIds(list).build();

        wishlistJPARepository.save(wishlistDocument);
    }

    @Given("a client with ID {string} and a list of products")
    public void aClientWithIDAndAListOfProductsProductList(String clientId, DataTable dataTable) {
        this.clientId = clientId;
        final var document = WishlistDocument.builder().clientId(clientId).productIds(dataTable.asList()).build();
        wishlistJPARepository.save(document);
    }

    @Given("a client with ID {string} without products")
    public void aClientWithIDWithoutProducts(String clientId) {
        this.clientId = clientId;
        wishlistJPARepository.save( WishlistDocument.builder().clientId(clientId).build());
    }

    @When("the client adds the product to their wishlist")
    public void theClientAddsTheProductToTheirWishlist() throws Exception {

        resultActions = mockMvc.perform(post("/clients/" + clientId + "/wishlist/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @When("the client checks if the product {string} is in their wishlist")
    public void theClientChecksIfTheProductIsInTheirWishlist(String productId) throws Exception {
        resultActions = mockMvc.perform(get("/clients/" + clientId + "/wishlist/products/" + productId));
    }

    @When("the client remove the product with ID {string} from their wishlist")
    public void theClientRemoveTheProductWithIDFromTheirWishlist(String productId) throws Exception {
        mockMvc.perform(delete("/clients/" + clientId + "/wishlist/products/" + productId)).andExpect(status().isOk());
    }

    @When("the client retrieves their wishlist")
    public void theClientRetrievesTheirWishlist() throws Exception {
        resultActions = mockMvc.perform(get("/clients/"+ clientId + "/wishlists"));
    }

    @Then("the product is added to the wishlist successfully")
    public void theProductIsAddedToTheWishlistSuccessfully() throws Exception {

        resultActions.andExpect(status().isCreated());
        var result = wishlistJPARepository.findByClientId(clientId);

        assertAll(() -> {

            assertTrue(result.isPresent());
            resultedWishlist = result.get();
            assertEquals(clientId, resultedWishlist.getClientId());

        });
    }

    @Then("the product with ID {string} was added to the wishlist successfully")
    public void theProductWithIDWasAddedToTheWishlistSuccessfully(String id) {
        assertTrue(resultedWishlist.getProductIds().contains(id));
    }

    @Then("the product is not added to the wishlist")
    public void theProductIsNotAddedToTheWishlist() throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    @Then("an error message {string} and an error code {string} is returned")
    public void anErrorMessageAndAnErrorCodeIsReturned(String message, String errorCode) throws Exception {
        resultActions.andExpect(content().json("{\"message\":\"" + message + "\",\"errorCode\":\"" + errorCode + "\"}"));
    }

    @Then("the wishlist is returned and contains")
    public void theWishlistContains(DataTable dataTable) throws Exception {

        var dataBaseList = dataTable.asList();
        List<String> expectedList = dataBaseList.get(0) != null ? dataBaseList : new ArrayList<>();
        resultActions.andExpect(status().isOk());

        final var wishlistResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), WishlistResponse.class);

        assertAll(() -> {

            var productList = wishlistResponse.getProductIds() != null ? wishlistResponse.getProductIds() : new ArrayList<String>();
            assertEquals(expectedList.size(), productList.size());
            assertEquals(clientId, wishlistResponse.getClientId());
            for (int i = 0; i < productList.size(); i++) {
                assertEquals(expectedList.get(i), productList.get(i));
            }
        });
    }

    @Then("a wishlist is not returned")
    public void aWishlistIsNotReturned() throws Exception {
        resultActions.andExpect(status().isNoContent());
        assertEquals(0, resultActions.andReturn().getResponse().getContentAsByteArray().length);
    }

    @Then("the product with ID {string} is not in the wishlist anymore")
    public void theProductWithIDIsNotInTheWishlistAnymore(String productId) {
        final var wishlist = wishlistJPARepository.findByClientId(clientId).get();
        assertFalse(wishlist.getProductIds().contains(productId));
    }

    @After
    public void cleanUp() {
        wishlistJPARepository.deleteAll();
        cacheManager.getCache("wishlist").clear();
    }
}
