package ru.proskuryakov.testcbrcursonadapter.adapter.models;

import lombok.Data;

import java.util.List;

@Data
public class CodesDatesHistoryRequest {

    private List<String> codes;
    private HistoryDateRequest dates;

}
