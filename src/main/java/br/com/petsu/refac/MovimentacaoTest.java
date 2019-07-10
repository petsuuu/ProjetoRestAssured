package br.com.petsu.refac;

import br.com.petsu.core.BaseTest;
import br.com.petsu.tests.Movimentacao;
import br.com.petsu.utils.DataUtils;
import br.com.petsu.utils.Utils;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveRemoverMovimentacao() {
        Integer MOVID = Utils.getIdMovPelaDescricao("Movimentacao para exclusao");


        given()
                .pathParam("id", MOVID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .log().all()
                .statusCode(204)
        ;
    }


    @Test
    public void naoDeveRemoverCOntaComMovimentacao() {
        Integer CONTA_ID = Utils.getIdContaPeloNome("Conta com movimentacao");

        given()
                .pathParam("id", CONTA_ID)
                .when()

                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", Matchers.is("transacoes_conta_id_foreign"))
        ;
    }



    public void naoDeveInserirMOvimentaçãoComDAtaFutura() {
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
    public void deveValidarCamposObrigatoriosMovimentacao() {
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
    public void deveInserirMovimentacaoSucesso() {


        Movimentacao mov = getMovimentacaoValida();


        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
        ;
    }


    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(Utils.getIdContaPeloNome("Conta para movimentacoes"));
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

}
