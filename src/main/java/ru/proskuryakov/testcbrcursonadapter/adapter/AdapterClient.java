package ru.proskuryakov.testcbrcursonadapter.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.CodeWithDates;
import ru.proskuryakov.testcbrcursonadapter.adapter.models.CursOnDate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class AdapterClient {

    private final RestTemplate restTemplate;

    public AdapterClient(@Value("${adapter.uri}") String rootUri, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(rootUri).build();
    }

    public CursOnDate getCursByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return this.restTemplate
                .exchange("/curs/{code}", HttpMethod.GET, requestEntity, CursOnDate.class, code)
                .getBody();

    }

    public CursOnDate getCursByCodeAndDate(String code, String date) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return this.restTemplate
                .exchange("/curs/{code}/date/{date}", HttpMethod.GET, requestEntity, CursOnDate.class, code, date)
                .getBody();
    }

    public CursOnDate[] getCursByDates(CodeWithDates codeWithDates){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CodeWithDates> requestEntity = new HttpEntity<>(codeWithDates, headers);

        return this.restTemplate
                .exchange("/curs", HttpMethod.POST, requestEntity, CursOnDate[].class)
                .getBody();
    }
}
