package tests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import core.BaseTest;

public class BarrigaTest extends BaseTest {
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "jonathan.linkedin2019@gmail.com");
		login.put("senha", "jhowjhow2");
		
		String token =
			given()
				
				.body(login)
			.when()
				.post("/signin")
			.then()
				.statusCode(200)
				.extract().path("token");
				
			given()
				.header("Authorization","JWT " + token)
				.body("{\"nome\": \"conta adicionada via api\"}")
			.when()
				.post("/contas")
			.then()
				.statusCode(201)
				
		;
	}
}
