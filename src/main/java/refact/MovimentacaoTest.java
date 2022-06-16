package refact;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tests.Movimentacao;
import utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

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
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

         given()
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
            .body("{}")
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(8))
            .body("msg", hasItems(

                    "Data da Movimentação é obrigatório",
                    "Data do pagamento é obrigatório",
                    "Descrição é obrigatório",
                    "Interessado é obrigatório",
                    "Valor é obrigatório",
                    "Valor deve ser um número",
                    "Conta é obrigatório",
                    "Situação é obrigatório"

            ))

        ;
    }

    @Test
    public void naoDeveInserirMovimentacaoFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DateUtils.getDataDiferencaDias(2));
        given()
            .body(mov)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))


        ;
    }

    @Test
    public  void naoDeveRemoverContaComMovimentacao(){
        Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");
        given()
            .pathParam("id", CONTA_ID)
        .when()
            .delete("contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"))

        ;

    }

    @Test
    public  void deveRemoverMovimentacao(){
        Integer MOV_ID = getMovimentacaoPelaDescricao("Movimentacao para exclusao");
        given()
            .pathParam("id", MOV_ID)
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204)

        ;

    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
        mov.setDescricao("Descrição da movimentação");
        mov.setEnvolvido("Envolvido na movimentação");
        mov.setTipo("REC");
        mov.setData_transacao(DateUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DateUtils.getDataDiferencaDias(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return  mov;
    }

    public Integer getIdContaPeloNome(String nome){
       return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
    public Integer getMovimentacaoPelaDescricao(String desc){
       return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
    }


}
