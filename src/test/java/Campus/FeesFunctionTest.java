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


public class FeesFunctionTest {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private String fees_id;

    HashMap<String, String> reqBody;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        reqBody = new HashMap<>();
        reqBody.put("name","Student Fees Test");
        reqBody.put("code","FTT");
        reqBody.put("priority","150");
        reqBody.put("active","true");
    }

    @Test
    public void loginTest(){

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password","TechnoStudy123");
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
    public void loginNegativeTest(){
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield");
        credentials.put("password","Richfield2020!");
        credentials.put("rememberMe", "true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .extract().detailedCookies();

    }

    @Test(dependsOnMethods = "loginNegativeTest")
    public void createFeesTest(){

        fees_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createFeesTest")
    public void getFeesTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBody.get("name")));

    }

    @Test(dependsOnMethods = "getFeesTest")
    public void createFeesNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createFeesNegativeTest")
    public void editFeesTest(){

        HashMap<String, String> editName = new HashMap<>();
        editName.put("id", fees_id);
        editName.put("name","Fees5 Test5");
        editName.put("code","FET");
        editName.put("priority","150");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editName)
                .when()
                .put("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(editName.get("name")))
                .body("code", equalTo(editName.get("code")));

    }

    @Test(dependsOnMethods = "editFeesTest")
    public void deleteFeesTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteFeesTest")
    public void getFeesNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "getFeesNegativeTest")
    public void deleteFeesNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(400);
    }

}
