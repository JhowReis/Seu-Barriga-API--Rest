package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import core.BaseTest;

public class BarrigaTest extends BaseTest {
	
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
		
	}
	
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
			given()
				.header("Authorization","JWT " + TOKEN)
				.body("{\"nome\": \"conta adicionada via api\"}")
			.when()
				.post("/contas")
			.then()
				.statusCode(201)
				
		;
	}
	@Test
	public void deveAlterarContaComSucesso() {
			given()
				.header("Authorization","JWT " + TOKEN)
				.body("{\"nome\": \"conta alterada via api\"}")
			.when()
				.put("/contas/1227698")
			.then()
				.statusCode(200)
				.body("nome", is("conta alterada via api"))
				.body("usuario_id", is(21441))
			
			;
	}
	
	@Test
	public void naoDeveIncluirContaComMesmoNome() {
		given()
			.header("Authorization","JWT " + TOKEN)
			.body("{\"nome\": \"conta alterada via api\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("J� existe uma conta com esse nome!"))
		
		;
	}

}
