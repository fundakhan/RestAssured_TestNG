import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GoRestNewUserTest {

    private RequestSpecification requestSpec; // this variable is local variable. (private) will be only specific to this class.

    private HashMap<String, String> requestBody;

    private Object userId;


    @BeforeClass
    public void setup(){

        RestAssured.baseURI ="https://gorest.co.in";

        requestSpec = given()
                .log().body() // this is response body. if you want you can use also log().uri()
                .header("Authorization", "Bearer d298b17124db095793f6f3aaa2db29a47d6a1ee26fc63870971b4aa104f22864")
                .contentType(ContentType.JSON);

        requestBody = new HashMap<>();
                       // key(name) value(Johnny Deep) structure
        // store all this information (put)
        requestBody.put("name","Johnny Deep");
        requestBody.put("email","johnnydeepp@yahoo.com");
        requestBody.put("gender","male");
        requestBody.put("status","active");

    }
    @Test
    public void createUserTest(){
    //Object user_id
        userId = given()
                .spec(requestSpec) // you can call request specification from setup
                .body(requestBody) // you can call request body from setup
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .body("name",equalTo(requestBody.get("name"))) // from the response body we are getting the name field
                .statusCode(201)
                .extract().path("id"); // get the id field from the response body.

    }

    @Test(dependsOnMethods = "createUserTest")
    public void createUserNegativeTest(){

         given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422);


    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void getUserAndValidate(){

        given()
                .spec(requestSpec)
                .when()
                .get("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(requestBody.get("name")))
                .body("email", equalTo(requestBody.get("email")))
                .body("gender", equalTo(requestBody.get("gender")))
                .body("status", equalTo(requestBody.get("status")));


    }

    @Test(dependsOnMethods = "getUserAndValidate")
    public void editUserTest(){
        HashMap<String,String> editName = new HashMap<>();
        editName.put("name","Johnny Cash5");

        given()
                .spec(requestSpec)
                .body(editName)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(editName.get("name")));
    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest(){

        given()
                .spec(requestSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest(){

        given()
                .spec(requestSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body() //for output message when we run
                .statusCode(404);

    }

    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail(){
        return RandomStringUtils.randomAlphabetic(8)+"@gmail.com";
    }


}
