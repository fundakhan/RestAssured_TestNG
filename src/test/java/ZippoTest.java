import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*; /** you need to import first */
import static org.hamcrest.Matchers.*;      /** you need to import too for hamcrest */

public class ZippoTest {

    @BeforeClass
    public void setup(){

        RestAssured.baseURI = "https://api.zippopotam.us";
    }

    @Test
    public void test(){ /** structure */

        given()
                .when()
                .then();

    }

    @Test
    public void checkingStatusCode(){

        given()
                .when()
                .get("/us/33326")
                .then()
                .statusCode(200);
    }

    @Test
    public void loggingRequestDetails(){

        given()
                .log().all()  /** you can add .log().body()  (specific) */
                .when()
                .get("/us/33326")
                .then();
    }

    @Test
    public void loggingResponseDetails(){

        given()
                .when()
                .get("/us/33326")
                .then()
                .log().body(); /** same here if you wanna see all details body use (.log().all()) */
    }

    @Test
    public void checkContentType(){

        given()
                .when()
                .get("/us/33326")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);

    }

    @Test
    public void validateCountryTest(){

        given()
                .when()
                .get("/us/33326")
                .then()
                .body("country",equalTo("United States"))
                .statusCode(200);
    }

    @Test
    public void validateCountryAbv(){
        given()
                .when()
                .get("/us/33326")
                .then()
                .body("'country abbreviation'",equalTo("US"))  /** if the field name has a space we gonna covert with single quotes("'country name'") */
                .statusCode(200);
    }

    @Test
    public void validateStateTest(){

        given()
                .when()
                .get("/us/33326")
                .then()
                .body("places[0].state",equalTo("Florida"))
                .statusCode(200);
    }

    @Test
    public void pathParameters(){

        String country = "us";
        String zipcode = "33326";

        given()
                .pathParams("country", country)
                .pathParams("zipcode", zipcode)
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .statusCode(200);
    }

    @Test
    public void queryParameters(){ /** query parameter -> specific search result */

    String gender = "female";
    String status = "active";

    given()
            .param("gender", gender) /** or you can write like this -> ("gender", "female") */
            .param("status", status) /** or you can write like this -> ("status", "active") */
            .when()
            .get("https://gorest.co.in/public/v2/users")
            .then()
            .statusCode(200)
            .log().body();

    }

    @Test
    public void extractValueTest(){

        Object countryInfo = given()
                .when()
                .get("/us/33326")
                .then()
                .extract().path("country");

        System.out.println(countryInfo);
    }

}
