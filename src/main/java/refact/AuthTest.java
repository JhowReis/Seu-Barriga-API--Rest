package refact;

import core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AuthTest extends BaseTest {

    private String TOKEN;


    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "jonathan.linkedin2019@gmail.com");
        login.put("senha", "jhowjhow2");

        String TOKEN =
            given()
                .body(login)
            .when()
                .post("/signin")
            .then()
                .statusCode(200)
                .extract().path("token");

        RestAssured.requestSpecification.header("Authorization","JWT " + TOKEN );

        //limpando o banco
        RestAssured.get("/reset").then().statusCode(200);
    }
    @Test
    public void naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");
        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }
}
