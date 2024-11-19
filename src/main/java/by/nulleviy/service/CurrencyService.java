package by.nulleviy.service;

import by.nulleviy.dao.CurrencyDao;
import by.nulleviy.dto.CurrencyDto;
import by.nulleviy.entity.Currency;
import by.nulleviy.entity.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CurrencyService {
    CurrencyDao currencyDao = new CurrencyDao();

    public void save(CurrencyDto currencyDto){
        Currency currency = new Currency(
                currencyDto.getId(),
                currencyDto.getCode(),
                currencyDto.getFullName(),
                currencyDto.getSign()
        );
        currencyDao.save(currency);
    }


    public Optional<CurrencyDto> findByCode(String code){
        if(currencyDao.findByCode(code)!=null){
            Currency currency = currencyDao.findByCode(code);
            return Optional.of(new CurrencyDto(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign()));
        }
        return Optional.empty();
    }

    public List<CurrencyDto> findAll() throws CustomException {
        List<Currency> currencies = currencyDao.findAll();
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