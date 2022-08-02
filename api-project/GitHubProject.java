package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GitHubProject {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String sshKey;
    int sshKeyId;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token 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")
                .setBaseUri("https://api.github.com")
                .build();
        sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCS+NfPf5gAQLPKLPvGCboc3Qb4JkkRqoI5HL4hYzDyZw+zq9U03ZyGRppP385f/ryXCvboKSeJS6J6OZ4S6/gVaKHmmWPRl5BdOiLVrtJIwyQuKtT9TBUHZumCf220gFt5q1Vei7D7DRUz78mHnFLVP4D64UpXa3iYX64iOQ2Bts/mPiEioI2JPoBK/bq4vEvKCzh0h8gPplTCtAWxH6htduQm9Wn8dQ1SQviSxUOWZuPFSfKXgpc4KN6rNL7+H1wNeCgB/7Xr0Q31XT4AyfvLx8Iq9Mt7+ZkszN6qpDgi+Wxi/kQEYp9q9JKSpqk/+yv0gXSCRDq8JMVFdT0lIxIP";

    }
    @Test(priority = 1)
    public void postSSHKey(){

        String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \""+sshKey+"\"}";
        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post("/user/keys");
        String resBody = response.getBody().asPrettyString();

        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        sshKeyId = response.then().extract().path("id");
        response.then().statusCode(201);

    }
    @Test(priority = 2)
    public void getSSHKey(){
        Response response = given().spec(requestSpec)
                .when().get("/user/keys");
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        response.then().statusCode(200);

    }

    @Test(priority = 3)
    public void deleteKey(){
        Response response = given().spec(requestSpec)
                .pathParam("keyId",sshKeyId).when().delete("/user/keys/{keyId}");
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        response.then().statusCode(204);

    }
}
