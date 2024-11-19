package by.nulleviy.dto;

import lombok.*;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Value
@Builder
public class CurrencyDto {
     Integer id;
     String code;
     String fullName;
     String sign;
}
