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
	
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(1227698);
		mov.setDescricao("Descri��o da movimenta��o");
		mov.setEnvolvido("Envolvido na movimenta��o");
		mov.setTipo("REC");
		mov.setData_transacao("01/02/2010");
		mov.setData_pagamento("01/01/2019");
		mov.setValor(100f);
		mov.setStatus(true);
		
		given()
			.header("Authorization","JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)

		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {
		given()
			.header("Authorization","JWT " + TOKEN)
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
					
					"Data da Movimenta��o � obrigat�rio",
					"Data do pagamento � obrigat�rio",
					"Descri��o � obrigat�rio",
					"Interessado � obrigat�rio",
					"Valor � obrigat�rio",
					"Valor deve ser um n�mero",
					"Conta � obrigat�rio",
					"Situa��o � obrigat�rio"

					))
		
		;
	}
	

}
