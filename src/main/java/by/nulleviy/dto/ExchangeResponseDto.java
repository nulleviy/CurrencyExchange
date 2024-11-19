package by.nulleviy.dto;

import by.nulleviy.entity.Currency;

import java.math.BigDecimal;

import lombok.*;

@NoArgsConstructor(force = true)
@Value
@Builder
@Getter
@AllArgsConstructor
public class ExchangeResponseDto {
    Integer id;
    Currency baseCurrencyId;
    Currency targetCurrencyId;
    BigDecimal rate;
    BigDecimal amount;
    BigDecimal convertedAmount;
}
