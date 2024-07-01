package com.wishlist;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.wishlist")
@ActiveProfiles(value = "tests")
public class CucumberTests {
    static {
        System.setProperty("spring.profiles.active", "tests");
    }
}
