package com.michonski.football.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoDto<T> {

    @Builder.Default
    T data = null;

    @Builder.Default
    String error = null;
}
