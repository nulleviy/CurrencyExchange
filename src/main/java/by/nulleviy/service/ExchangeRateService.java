package by.nulleviy.service;

import by.nulleviy.dao.CurrencyDao;
import by.nulleviy.dao.ExchangeRateDao;
import by.nulleviy.dto.*;
import by.nulleviy.entity.Currency;
import by.nulleviy.entity.ExchangeRate;
import by.nulleviy.entity.exception.CustomException;

import java.math.BigDecimal;
import java.math.MathContext;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final Currency dollar = currencyDao.findByCode("USD");


    public ExchangeResponseDto exchange(String baseCurrencyCode, String targetCurrencyCode,String amountParam) throws CustomException {
        if(exchangeRateDao.findByCodes(baseCurrencyCode,targetCurrencyCode)!=null){
            return directExchange(baseCurrencyCode,targetCurrencyCode,amountParam);

        }else if(exchangeRateDao.findByCodes(baseCurrencyCode,targetCurrencyCode)==null && exchangeRateDao.findByCodes(targetCurrencyCode,baseCurrencyCode)!=null){
            return reverseExchange(baseCurrencyCode,targetCurrencyCode,amountParam);

        }else if(exchangeRateDao.findByCodes(dollar.getCode(),baseCurrencyCode)!=null && exchangeRateDao.findByCodes(dollar.getCode(),targetCurrencyCode)!=null){
            return crossExchange(baseCurrencyCode,targetCurrencyCode,amountParam);
        }else{
            throw new CustomException("Что-то пошло не так (exchange)");
        }
    }

    public ExchangeResponseDto directExchange(String baseCurrencyCode, String targetCurrencyCode, String amountParam){
        BigDecimal amount = new BigDecimal(amountParam);
        BigDecimal rate = exchangeRateDao.findByCodes(baseCurrencyCode,targetCurrencyCode).getRate();
        BigDecimal convertedAmount = amount.multiply(rate,MathContext.DECIMAL64);

        return new ExchangeResponseDto(
                0,
                currencyDao.findByCode(baseCurrencyCode),
                currencyDao.findByCode(targetCurrencyCode),
                exchangeRateDao.findByCodes(baseCurrencyCode,targetCurrencyCode).getRate(),
                amount,
                convertedAmount
                );
    }

    public ExchangeResponseDto crossExchange(String baseCurrencyCode, String targetCurrencyCode, String amountParam){

        BigDecimal amount = new BigDecimal(amountParam);
        BigDecimal firstRate = exchangeRateDao.findByCodes(dollar.getCode(),baseCurrencyCode).getRate();
        BigDecimal secondRate = exchangeRateDao.findByCodes(dollar.getCode(),targetCurrencyCode).getRate();
        BigDecimal crossRate = firstRate.divide(secondRate,MathContext.DECIMAL64);
        BigDecimal convertedAmount = amount.multiply(crossRate);

        ExchangeRate exchangeRate = new ExchangeRate(0,currencyDao.findByCode(baseCurrencyCode),currencyDao.findByCode(targetCurrencyCode),crossRate);

        return new ExchangeResponseDto(
                exchangeRate.getId(),
                currencyDao.findById(exchangeRate.getBaseCurrencyId().getId()),
                currencyDao.findById(exchangeRate.getTargetCurrencyId().getId()),
                crossRate,
                amount,
                convertedAmount
        );
    }

    public ExchangeResponseDto reverseExchange(String baseCurrencyCode, String targetCurrencyCode, String amountParam){
            ExchangeRate exchangeRate = exchangeRateDao.findByCodes(targetCurrencyCode,baseCurrencyCode);
            BigDecimal amount = new BigDecimal(amountParam);
            BigDecimal rate = exchangeRateDao.findByCodes(targetCurrencyCode,baseCurrencyCode).getRate();
            BigDecimal reverseRate = BigDecimal.ONE.divide(rate,MathContext.DECIMAL64);
            BigDecimal convertedAmount = amount.multiply(reverseRate);

            return new ExchangeResponseDto(
                    exchangeRate.getId(),
                    currencyDao.findById(exchangeRate.getBaseCurrencyId().getId()),
                    currencyDao.findById(exchangeRate.getTargetCurrencyId().getId()),
                    reverseRate,
                    amount,
                    convertedAmount
            );


    }
    public Optional<ExchangeRateResponseDto> findByCodes(String code1, String code2){
        if(exchangeRateDao.findByCodes(code1,code2)!=null) {
            ExchangeRate exchangeRate = exchangeRateDao.findByCodes(code1, code2);
            return Optional.of(new ExchangeRateResponseDto(
                    exchangeRate.getId(),
                    new Currency(
                            exchangeRate.getBaseCurrencyId().getId(),
                            exchangeRate.getBaseCurrencyId().getCode(),
                            exchangeRate.getBaseCurrencyId().getFullName(),
                            exchangeRate.getBaseCurrencyId().getSign()
                    ),
                    new Currency(
                            exchangeRate.getTargetCurrencyId().getId(),
                            exchangeRate.getTargetCurrencyId().getCode(),
                            exchangeRate.getTargetCurrencyId().getFullName(),
                            exchangeRate.getTargetCurrencyId().getSign()
                    ),
                    exchangeRate.getRate()));
        }
        return Optional.empty();
    }
    public ExchangeRateResponseDto findById(int id){
        ExchangeRate exchangeRate = exchangeRateDao.findById(id);
        return new ExchangeRateResponseDto(
                    exchangeRate.getId(),
                new Currency(
                exchangeRate.getBaseCurrencyId().getId(),
                exchangeRate.getBaseCurrencyId().getCode(),
                exchangeRate.getBaseCurrencyId().getFullName(),
                exchangeRate.getBaseCurrencyId().getSign()
                ),
                new Currency(
                exchangeRate.getTargetCurrencyId().getId(),
                exchangeRate.getTargetCurrencyId().getCode(),
                exchangeRate.getTargetCurrencyId().getFullName(),
                exchangeRate.getTargetCurrencyId().getSign()
                ),
                    exchangeRate.getRate()
            );
    }
    public List<ExchangeRateResponseDto> findAll(){
        List<ExchangeRate> exchangeRateList = exchangeRateDao.findAll();
        return exchangeRateList.stream().map(exchangeRate -> new ExchangeRateResponseDto(
                exchangeRate.getId(),
                new Currency(
                        exchangeRate.getBaseCurrencyId().getId(),
                        exchangeRate.getBaseCurrencyId().getCode(),
                        exchangeRate.getBaseCurrencyId().getFullName(),
                        exchangeRate.getBaseCurrencyId().getSign()
                ),
                new Currency(
                        exchangeRate.getTargetCurrencyId().getId(),
                        exchangeRate.getTargetCurrencyId().getCode(),
                        exchangeRate.getTargetCurrencyId().getFullName(),
                        exchangeRate.getTargetCurrencyId().getSign()
                ),
                exchangeRate.getRate()
        )).collect(toList());
    }

    public void update(ExchangeRateRequestDto exchangeRateRequestDto){
        ExchangeRate exchangeRate = new ExchangeRate(
                exchangeRateRequestDto.getId(),
                currencyDao.findByCode(exchangeRateRequestDto.getBaseCurrencyId()),
                currencyDao.findByCode(exchangeRateRequestDto.getTargetCurrencyId()),
                exchangeRateRequestDto.getRate()
        );
        exchangeRateDao.update(exchangeRate);
    }

    public void save(ExchangeRateRequestDto exchangeRateRequestDto){
        ExchangeRate exchangeRate = new ExchangeRate(
                exchangeRateRequestDto.getId(),
                currencyDao.findByCode(exchangeRateRequestDto.getBaseCurrencyId()),
                currencyDao.findByCode(exchangeRateRequestDto.getTargetCurrencyId()),
                exchangeRateRequestDto.getRate()
        );

        exchangeRateDao.save(exchangeRate);
    }

    private static ExchangeRate getExchangeInCode(List<ExchangeRate> rates, String code){
        return rates.stream()
                .filter(rate -> rate.getTargetCurrencyId().getCode().equals(code))
                .findFirst()
                .orElseThrow();
    }
}
