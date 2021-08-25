package ru.proskuryakov.testcbrcursonadapter.adapter.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CursOnDate {

    private String code;
    private BigDecimal curs;
    private String date;

}
