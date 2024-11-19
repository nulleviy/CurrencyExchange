package by.nulleviy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
@Builder
@NoArgsConstructor(force = true)
public class ExchangeRateRequestDto {
    Integer id;
    String baseCurrencyId;
    String targetCurrencyId;
    BigDecimal rate;
}
