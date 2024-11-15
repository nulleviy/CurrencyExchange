package by.nulleviy.service;

import by.nulleviy.dao.impl.CurrencyDao;
import by.nulleviy.entity.dto.CurrencyDto;
import by.nulleviy.entity.Currency;
import by.nulleviy.entity.exception.CustomException;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class CurrencyService {
    public static List<CurrencyDto> findAll() throws CustomException {
        List<Currency> currencies = CurrencyDao.findAll();
        if(currencies.isEmpty()){
            throw new CustomException("У тебя пустой список");
        }
        return currencies.stream().map(currency -> new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign()
                ))
                .collect(toList());
    }
}
