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


public class CitizenshipFunctionTest {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private String citizen_id;

    HashMap<String, String> reqBody;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);


        reqBody = new HashMap<>();
        reqBody.put("name","US Citizen");
        reqBody.put("shortName", "USA");

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
    public void createCitizenshipTest(){

        citizen_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createCitizenshipTest")
    public void getCitizenshipTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBody.get("name")));

    }

    @Test(dependsOnMethods = "getCitizenshipTest")
    public void createCitizenshipNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createCitizenshipNegativeTest")
    public void editCitizenshipTest(){

        HashMap<String, String> editName = new HashMap<>();
        editName.put("id", citizen_id);
        editName.put("name", "Turkey Citizen");
        editName.put( "shortName", "TUR");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editName)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(editName.get("name")))
                .body("shortName", equalTo(editName.get("shortName")));

    }

    @Test(dependsOnMethods = "editCitizenshipTest")
    public void deleteCitizenshipTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteCitizenshipTest")
    public void getCitizenshipNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "getCitizenshipNegativeTest")
    public void deleteCitizenshipNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(400);
    }

}
