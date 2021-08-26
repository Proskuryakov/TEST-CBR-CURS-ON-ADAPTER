package ru.proskuryakov.testcbrcursonadapter.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggingMessage {

    private String action;
    private Date datetime;

}

