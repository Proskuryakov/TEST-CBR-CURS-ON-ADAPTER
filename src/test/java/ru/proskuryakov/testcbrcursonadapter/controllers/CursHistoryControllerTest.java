package ru.proskuryakov.testcbrcursonadapter.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.IntervalModel;
import ru.proskuryakov.testcbrcursonadapter.rabbit.LoggingMessage;
import ru.proskuryakov.testcbrcursonadapter.rabbit.RabbitLoggingListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CursHistoryControllerTest {


    @Autowired
    private RabbitLoggingListener loggingListener;

    @Value("${adapter.uri}")
    private String url;

    private IntervalModel intervalModel;

    @BeforeEach
    void setUp() {
        intervalModel = new IntervalModel(null, 34L, "JPY", 333, true);
    }

    @Test
    @DisplayName("addInterval")
    public void test_0001() {
        IntervalModel createdIntervalModel =
                given().log().body().contentType("application/json")
                        .when()
                        .body(intervalModel)
                        .post(url + "/history/interval")
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .contentType("application/json")
                        .extract()
                        .as(IntervalModel.class);

        intervalModel.setId(createdIntervalModel.getId());
        assertThat(createdIntervalModel, Matchers.equalTo(intervalModel));
    }

    @Test
    @DisplayName("Rabbit logging addInterval test")
    public void test_0002() {
        LoggingMessage loggingMessage = loggingListener.getMessage();
        assertThat(loggingMessage, is(notNullValue()));
        assertThat(loggingMessage.getAction(), Matchers.equalTo("addInterval"));
    }

    @Test
    @DisplayName("getIntervalByCode")
    public void test_0003() {
        assert intervalModel.getValuteId() != null;
        given()
                .log()
                .body()
                .contentType("application/json")
                .when()
                .get(url + "/history/interval/" + intervalModel.getCode())
                .then()
                .log()
                .body()
                .statusCode(200)
                .body("interval", Matchers.equalTo(intervalModel.getInterval()))
                .body("code", Matchers.equalTo(intervalModel.getCode()))
                .body("valuteId", Matchers.equalTo(intervalModel.getValuteId().intValue()))
                .body("isActual", Matchers.equalTo(intervalModel.getIsActual()));

    }

    @Test
    @DisplayName("Rabbit logging getIntervalByCode test")
    public void test_0004() {
        LoggingMessage loggingMessage = loggingListener.getMessage();
        assertThat(loggingMessage, is(notNullValue()));
        assertThat(loggingMessage.getAction(), Matchers.equalTo("getIntervalByCode"));
    }

    @Test
    @DisplayName("deleteIntervalByCode")
    public void test_0005() {
        given().log().body().contentType("application/json")
                .when()
                .delete(url + "/history/interval/" + intervalModel.getCode())
                .then()
                .log()
                .body()
                .statusCode(200);
    }

    @Test
    @DisplayName("Rabbit logging deleteIntervalByCode test")
    public void test_0006() {
        LoggingMessage loggingMessage = loggingListener.getMessage();
        assertThat(loggingMessage, is(notNullValue()));
        assertThat(loggingMessage.getAction(), Matchers.equalTo("deleteIntervalByCode"));
    }

}
