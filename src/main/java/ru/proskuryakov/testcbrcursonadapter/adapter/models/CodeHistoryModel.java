package ru.proskuryakov.testcbrcursonadapter.adapter.models;

import lombok.Data;

import java.util.List;

@Data
public class CodeHistoryModel {

    private String code;
    private List<HistoryModel> history;

}
