package by.nulleviy.dto;

import by.nulleviy.entity.Currency;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Value
@Builder
@ToString
public class ExchangeRateResponseDto {
    Integer id;
    Currency baseCurrencyId;
    Currency targetCurrencyId;
    BigDecimal rate;
}
