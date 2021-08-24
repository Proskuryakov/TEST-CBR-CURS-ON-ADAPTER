package ru.proskuryakov.testcbrcursonadapter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.proskuryakov.testcbrcursonadapter.adapter.AdapterClient;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.CodeWithDates;
import ru.proskuryakov.testcbrcursonadapter.cbr.CbrClient;
import ru.proskuryakov.testcbrcursondateadapter.cbr.wsdl.ValuteData;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootApplication
public class TestCbrCursOnAdapterApplication implements CommandLineRunner {

    private final CbrClient cbrClient;
    private final AdapterClient adapterClient;

    public TestCbrCursOnAdapterApplication(CbrClient cbrClient, AdapterClient adapterClient) {
        this.cbrClient = cbrClient;
        this.adapterClient = adapterClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestCbrCursOnAdapterApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        cbrClient.getValuteCursOnDate(new GregorianCalendar(2021, Calendar.AUGUST, 24)).stream()
                .map(this::convertToString)
                .forEach(System.out::println);

        System.out.println(adapterClient.getCursByCode("USD"));
        System.out.println(adapterClient.getCursByCodeAndDate("USD", "2021-01-01"));
        CodeWithDates codeWithDates = new CodeWithDates();
        codeWithDates.setCode("USD");
        codeWithDates.setDates(List.of("1997-01-01", "1998-01-01"));
        System.out.println(Arrays.toString(adapterClient.getCursByDates(codeWithDates)));

    }

    private String convertToString(ValuteData.ValuteCursOnDate valuteCursOnDate) {
        return String.join(", ", List.of(
                valuteCursOnDate.getVchCode(),
                String.valueOf(valuteCursOnDate.getVcurs()))
        );
    }


}
