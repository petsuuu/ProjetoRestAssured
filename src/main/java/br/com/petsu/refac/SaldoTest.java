package br.com.petsu.refac;

import br.com.petsu.core.BaseTest;
import br.com.petsu.utils.Utils;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class SaldoTest extends BaseTest {


    @Test
    public void t09_deveCalcularSaldoContas() {
        Integer CONTA_ID = Utils.getIdContaPeloNome("Conta para saldo");


        given()
                .when()
                .get("/saldo")
                .then()
                .log().all()
                .statusCode(200)
                .body("find{it.conta_id == " + CONTA_ID + "}.saldo", Matchers.is("534.00"))
        ;
    }


}
