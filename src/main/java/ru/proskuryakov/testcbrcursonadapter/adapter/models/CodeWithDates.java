package ru.proskuryakov.testcbrcursonadapter.adapter.models;

import lombok.Data;

import java.util.List;

@Data
public class CodeWithDates {

    private String code;
    private List<String> dates;

}
