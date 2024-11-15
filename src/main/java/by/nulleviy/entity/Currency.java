package by.nulleviy.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
@Getter
@NoArgsConstructor(force = true)
@Value
@Builder
public class Currency {
    int id;
    String code;
    String fullName;
    String sign;

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

}
