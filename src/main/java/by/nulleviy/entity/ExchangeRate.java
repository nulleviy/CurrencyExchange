package by.nulleviy.entity;

import java.math.BigDecimal;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor(force = true)
@Value
public class ExchangeRate {
    Integer id;
    Currency baseCurrencyId;
    Currency targetCurrencyId;
    BigDecimal rate;

    public ExchangeRate(Integer id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate){
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
