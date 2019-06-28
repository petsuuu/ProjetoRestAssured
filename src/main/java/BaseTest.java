import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

public class BaseTest implements Constantes {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = APP_BASE_URl;
        RestAssured.port = APP_PORT;
        RestAssured.basePath = APP_BASE_PATH;
        RestAssured.proxy(APP_PROXY, APP_PROXY_PORT);

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        RestAssured.responseSpecification = resBuilder.build();

        //Habilitar log de erro caso ocorra erro
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }
}
