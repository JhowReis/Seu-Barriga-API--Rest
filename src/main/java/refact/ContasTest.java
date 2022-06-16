package refact;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ContasTest {

    private String TOKEN;


    @Before
    public void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "jonathan.linkedin2019@gmail.com");
        login.put("senha", "jhowjhow2");

        TOKEN =
            given()
                .body(login)
            .when()
                .post("/signin")
            .then()
                .statusCode(200)
                .extract().path("token");

        RestAssured.requestSpecification.header("Authorization","JWT " + TOKEN );

        RestAssured.get("/reset").then().statusCode(200);
    }
    @Test
    public void deveIncluirContaComSucesso() {
        String CONTA_ID =
                given()
                        .header("Authorization","JWT " + TOKEN)
                        .body("{\"nome\": \"conta inserida\"}")
                        .when()
                        .post("/contas")
                        .then()
                        .statusCode(201)
                        .extract().path("id")

        ;
    }
}
