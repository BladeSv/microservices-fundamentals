package by.mitrakhovich.resourceservice.controller.cucumber;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", glue = {"by.mitrakhovich.resourceservice.controller.cucumber"})
public class ResourceControllerComponentTest {
}
