package Campus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CountriesFunctionTest {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private String country_id;


    @BeforeClass
    public void setup(){

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

    }

    @Test
    public void loginTest(){

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password","Richfield2020!");
        credentials.put("rememberMe", "true");

       cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();
    }

    @Test(dependsOnMethods = "loginTest")
    public void createCountryTest(){

        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name","Turkiyeee1");
        reqBody.put( "code", "TUR");
        reqBody.put("hasState", "true");


        country_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .body("code", equalTo(reqBody.get("code")))
                .extract().jsonPath().getString("id");
    }


    @Test(dependsOnMethods = "createCountryTest")
    public void editCountryTest(){

        HashMap<String, String> updateName = new HashMap<>();
        updateName.put("id", country_id);
        updateName.put("name","Nepalll5");
        updateName.put( "code", "NPL");
        updateName.put("hasState", "false");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateName)
                .when()
                .put("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateName.get("name")));


    }

    @Test(dependsOnMethods = "editCountryTest")
    public void deleteCountryTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + country_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteCountryTest")
    public void deleteCountryNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + country_id)
                .then()
                .log().body()
                .statusCode(400);

    }
}
