package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> reqHeaders = new HashMap<>();
    //Resource path
    String resourcePath = "/api/users";

    //Create the contract
    @Pact(consumer ="UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
      //set the headers
      reqHeaders.put("Content-Type", "application/json");

      //create the JSON body
        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id", 123)
                .stringType("firstName", "Susanta")
                .stringType("lastName", "Kumar")
                .stringType("email", "susanta@test.com");
        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .headers(reqHeaders)
                .path(resourcePath)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider", port="8282")
    public void consumerSideTest(){
        //set baseURI
        String baseURI = "http://localhost:8282";

        //Request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Susanta");
        reqBody.put("lastName", "Kumar");
        reqBody.put("email","susanta@test.com");

        //Generate response
       given().headers(reqHeaders).body(reqBody).
        when().post(baseURI+resourcePath).
        then().log().all().statusCode(201);



    }

}
