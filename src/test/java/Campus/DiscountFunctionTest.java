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


public class DiscountFunctionTest {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private String discounts_id;

    HashMap<String, String> reqBody;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);


        reqBody = new HashMap<>();
        reqBody.put("description","Alumni Batch1 Discount");
        reqBody.put("code", "QA Engineer");
        reqBody.put("active", "true");
        reqBody.put("priority", "100");
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
    public void createDiscountTest(){

        discounts_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("description", equalTo(reqBody.get("description")))
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createDiscountTest")
    public void getDiscountTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discounts_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(reqBody.get("description")));

    }

    @Test(dependsOnMethods = "getDiscountTest")
    public void createDiscountNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createDiscountNegativeTest")
    public void editDiscountTest(){

        HashMap<String, String> editName = new HashMap<>();
        editName.put("id", discounts_id);
        editName.put("description", "Students Discount");
        editName.put( "code", "SDET");
        editName.put( "active", "true");
        editName.put( "priority", "100");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editName)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(editName.get("description")))
                .body("code", equalTo(editName.get("code")));

    }

    @Test(dependsOnMethods = "editDiscountTest")
    public void deleteDiscountTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + discounts_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDiscountTest")
    public void getDiscountNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discounts_id)
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "getDiscountNegativeTest")
    public void deleteDiscountNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + discounts_id)
                .then()
                .log().body()
                .statusCode(400);
    }



}
