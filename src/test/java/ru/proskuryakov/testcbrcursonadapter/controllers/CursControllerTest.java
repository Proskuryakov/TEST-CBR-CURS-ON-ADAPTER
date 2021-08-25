package ru.proskuryakov.testcbrcursonadapter.controllers;

import io.restassured.config.DecoderConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.bind.annotation.RequestBody;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.CodeWithDates;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.CursOnDate;
import ru.proskuryakov.testcbrcursonadapter.cbr.CbrService;
import ru.proskuryakov.testcbrcursonadapter.utils.TestUtils;
import ru.proskuryakov.testcbrcursondateadapter.cbr.wsdl.ValuteData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CursControllerTest {

    @Autowired
    private CbrService cbrService;

    @Value("${adapter.uri}")
    private String url;

    @Test
    public void getCursByCodeTest() {
        String code = "USD";
        GregorianCalendar date = new GregorianCalendar();

        ValuteData.ValuteCursOnDate valuteCursOnDate = cbrService.getByCodeAndDate(code, date);

        CursOnDate cursOnDate =
                given().log().body().contentType("application/json")
                        .when()
                        .get(url + "/curs/" + code)
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .contentType("application/json")
                        .extract()
                        .as(CursOnDate.class);

        assertThat(cursOnDate.getCurs(), Matchers.equalTo(valuteCursOnDate.getVcurs()));
        assertThat(cursOnDate.getCode(), Matchers.equalTo(valuteCursOnDate.getVchCode()));
        assertThat(cursOnDate.getDate(), Matchers.equalTo(TestUtils.dateToString(date)));
    }

    @Test
    public void getCursByCodeAndDateTest() {
        String code = "USD";
        GregorianCalendar calendar = new GregorianCalendar(2020, Calendar.FEBRUARY, 20);
        String strDate = TestUtils.dateToString(calendar);

        ValuteData.ValuteCursOnDate valuteCursOnDate = cbrService.getByCodeAndDate(code, calendar);

        CursOnDate cursOnDate =
                given().log().body().contentType("application/json")
                        .when()
                        .get(url + "/curs/" + code + "/date/" + strDate)
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .contentType("application/json")
                        .extract()
                        .as(CursOnDate.class);

        assertThat(cursOnDate.getCurs(), Matchers.equalTo(valuteCursOnDate.getVcurs()));
        assertThat(cursOnDate.getCode(), Matchers.equalTo(valuteCursOnDate.getVchCode()));
        assertThat(cursOnDate.getDate(), Matchers.equalTo(strDate));
    }

    @Test
    public void getCursByDates() {
        String code = "USD";
        List<String> dates = List.of("2001-01-17", "2021-01-17");

        CodeWithDates codeWithDates = new CodeWithDates();
        codeWithDates.setCode(code);
        codeWithDates.setDates(dates);

        ValuteData.ValuteCursOnDate first = cbrService.getByCodeAndDate(code, new GregorianCalendar(2001, Calendar.JANUARY, 17));
        ValuteData.ValuteCursOnDate second = cbrService.getByCodeAndDate(code, new GregorianCalendar(2021, Calendar.JANUARY, 17));

        var curses = given()
                .contentType(ContentType.JSON)
                .body(codeWithDates)
                .when()
                .post(url + "/curs")
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .body("", Matchers.hasSize(2))
                .extract()
                .as(CursOnDate[].class);

        assertThat(curses[0].getDate(), Matchers.equalTo(dates.get(0)));
        assertThat(curses[0].getCurs(), Matchers.equalTo(first.getVcurs()));
        assertThat(curses[0].getCode(), Matchers.equalTo(first.getVchCode()));

        assertThat(curses[1].getDate(), Matchers.equalTo(dates.get(1)));
        assertThat(curses[1].getCurs(), Matchers.equalTo(second.getVcurs()));
        assertThat(curses[1].getCode(), Matchers.equalTo(second.getVchCode()));
    }


}
