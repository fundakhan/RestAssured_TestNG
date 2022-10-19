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

public class NationalitiesFunctionTest {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String nationality_id;
    HashMap<String, String> reqBody;

    @BeforeClass
    public void setup(){

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        reqBody = new HashMap<>();
        reqBody.put("name","Nativaaa9516");

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
    public void createNationalityTest(){

        nationality_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createNationalityTest")
    public void createNationalityNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createNationalityNegativeTest")
    public void getNationalityTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBody.get("name")));

    }

    @Test(dependsOnMethods = "getNationalityTest")
    public void editNationalityTest(){

        HashMap<String, String> editName = new HashMap<>();
        editName.put("id", nationality_id);
        editName.put("name", "Nation1567");
        editName.put( "translateName", null);

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editName)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(editName.get("name")));

    }



    @Test(dependsOnMethods = "editNationalityTest")
    public void deleteNationalityTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteNationalityTest")
    public void getNationalityNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(400);


    }

    @Test(dependsOnMethods = "getNationalityNegativeTest")
    public void deleteNationalityNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(400);
    }






}
