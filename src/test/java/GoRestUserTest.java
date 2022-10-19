import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GoRestUserTest {

    /** Encapsulation */
    private RequestSpecification reqSpec;
    private HashMap<String, String> requestBody; /** i declared hashmap */

    private  Object userId;

    @BeforeClass
    public void setup(){

        RestAssured.baseURI = "https://gorest.co.in";

         reqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer d298b17124db095793f6f3aaa2db29a47d6a1ee26fc63870971b4aa104f22864")
                .contentType(ContentType.JSON);

        requestBody = new HashMap<>(); /** then i initialized here hashmap */
        requestBody.put("name","Caglaaa Sikelll");
        requestBody.put("email","caglasikel55@yahoo.com");
        requestBody.put("gender","female");
        requestBody.put("status","active");

        
    }

    @Test
    public void createUserTest(){

        userId = given()
                .spec(reqSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .body("name",equalTo(requestBody.get("name"))) /** hamcrest */
                .statusCode(201)
                .extract().path("id"); /** storing the "id" of user inside "userId" */
    }

    @Test(dependsOnMethods = "createUserTest")
    public void editUserTest(){

        HashMap<String,String> editName = new HashMap<>();
        editName.put("name","Toffee Toffee");
        given()
                .spec(reqSpec)
                .body(editName)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest(){

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest(){

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(404);
    }


}
