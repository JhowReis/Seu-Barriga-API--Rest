package refact;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ContasTest  extends BaseTest {

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
    public void deveIncluirContaComSucesso() {
                given()
                    .body("{ \"nome\": \"Conta inserida\" }")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)

        ;
    }
    @Test
    public void deveAlterarContaComSucesso() {
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");
        given()
            .body("{\"nome\": \"Conta alterada\"}")
            .pathParam("id", CONTA_ID)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is("Conta alterada"))
        ;
    }


    @Test
    public void naoDeveIncluirContaComMesmoNome() {
        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))

        ;
    }
    public Integer getIdContaPeloNome(String nome){
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
}
