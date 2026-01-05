package com.ensa.SprintFlow.dto.acceptanceCriteria;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AcceptanceCriteriaDto {
    @NotBlank
    private String given;
    @NotBlank
    private String when;
    private List<String> ands;
    @NotBlank
    private String then;
}
