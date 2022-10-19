import Pojo.GoRestUser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoRestUserTestWithPojo {

    private RequestSpecification requestSpec;

    private GoRestUser user;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in";

         requestSpec = given()
                .log().body()
                .header("Authorization", "Bearer d298b17124db095793f6f3aaa2db29a47d6a1ee26fc63870971b4aa104f22864")
                .contentType(ContentType.JSON);

         user = new GoRestUser(); // i created my GoRestUser object
         user.setName("Toffee");  // i stored my information inside the object
         user.setEmail("tofff@yahoo.com");
         user.setGender("male");
         user.setStatus("active");
    }

    @Test
    public void createUserTest(){


       user.setId(given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(user.getName()))
                .extract().jsonPath().getString("id")); // we are getting the id field as a string
    }

    @Test(dependsOnMethods = "createUserTest")
    public void createUserNegativeTest(){

        given()
                .spec(requestSpec)
                .body(user)
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
                .get("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(user.getName()))
                .body("email", equalTo(user.getEmail()))
                .body("gender", equalTo(user.getGender()))
                .body("status", equalTo(user.getStatus()));

    }

    @Test(dependsOnMethods = "getUserAndValidate")
    public void editUserTest(){
        HashMap<String,String> editName = new HashMap<>();
        editName.put("status","inactive");

        given()
                .spec(requestSpec)
                .body(editName)
                .when()
                .put("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("status", equalTo(editName.get("status")));
    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest(){

        given()
                .spec(requestSpec)
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest(){

        given()
                .spec(requestSpec)
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body() //for output message when we run
                .statusCode(404);

    }



}
