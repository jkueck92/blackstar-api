package de.jkueck.blackstar.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationCreateDto {

    private String keyword;

    private String keywordText;

    private String location;

    private LocalDateTime timestamp;

}
