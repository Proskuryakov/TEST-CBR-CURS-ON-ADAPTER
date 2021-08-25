package ru.proskuryakov.testcbrcursonadapter.cbr;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.proskuryakov.testcbrcursondateadapter.cbr.wsdl.ValuteData;

import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CbrService {

    private final CbrClient cbrClient;

    public ValuteData.ValuteCursOnDate getByCodeAndDate(String code, GregorianCalendar date) {
        List<ValuteData.ValuteCursOnDate> result = cbrClient.getValuteCursOnDate(date);

        return result.stream().filter(v -> v.getVchCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

}
