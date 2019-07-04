package br.com.petsu.refac.suite;


import br.com.petsu.core.BaseTest;
import br.com.petsu.refac.AuthTest;
import br.com.petsu.refac.ContasTest;
import br.com.petsu.refac.MovimentacaoTest;
import br.com.petsu.refac.SaldoTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void login() {
        System.out.println("Before Conta");
        Map <String, String> login = new HashMap <String, String>();
        login.put("email", "peterson.cardoso@me.com");
        login.put("senha", "123456");

        String TOKEN =
                given()
                        .body(login)
                        .when()
                        .log().all()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.headers("Authorization", "JWT " + TOKEN);

        RestAssured.get("/reset").then().statusCode(200);

    }

}
