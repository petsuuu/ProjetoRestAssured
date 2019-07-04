package br.com.petsu.tests;

import br.com.petsu.utils.DataUtils;
import br.com.petsu.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NewTest extends BaseTest {


    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;
    private static Integer MOV_ID;


    @Test
    public void t02_deveIncluirContaComSucesso() {
        CONTA_ID = given()
                .body("{\"nome\": \"" + CONTA_NAME + "\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
                .extract().path("id")
        ;
    }




    //Criar um metodo para facilitar chamadas
    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(CONTA_ID);
        //  mov.setUsuario_id(usuario_id);
        mov.setDescricao("Descricao da movimentacao");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }


    @Test
    public void t05_deveInserirMovimentacaoSucesso() {
        Movimentacao mov = getMovimentacaoValida();


        MOV_ID = given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id")
        ;
    }

    @Test
    public void t06_deveValidarCamposObrigatoriosMovimentacao() {
        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", Matchers.hasSize(8))
                .body("msg", Matchers.hasItems(
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
    public void t07_naoDeveInserirMOvimentaçãoComDAtaFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

        given()

                .log().all()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .log().all()
                .statusCode(400)
//                .body("$", Matchers.hasSize(1))
        //               .body("msg", Matchers.hasItems("Data da Movimentação deve ser menor ou igual à data atual"))

        ;
    }

    @Test
    public void t08_naoDeveRemoverCOntaComMovimentacao() {
        given()
                .pathParam("id", CONTA_ID)
                .when()

                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", Matchers.is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void t09_deveCalcularSaldoContas() {
        given()
                .when()
                .get("/saldo")
                .then()
                .log().all()
                .statusCode(200)
                .body("find{it.conta_id == " + CONTA_ID + "}.saldo", Matchers.is("100.00"))
        ;
    }

    @Test
    public void t10_deveRemoverMovimentacao() {
        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void t11_naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        given()
                .when()
                .get("/contas")
                .then()
                .log().all()
                .statusCode(401)
        ;
    }

}
