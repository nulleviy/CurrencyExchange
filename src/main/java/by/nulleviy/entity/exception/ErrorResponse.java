package by.nulleviy.entity.exception;

import lombok.*;

@AllArgsConstructor
@Value
@Builder
@Setter
@Getter

public class ErrorResponse{
    int id;
    String code;
}
