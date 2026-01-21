package com.ensa.SprintFlow.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserStoryMetrics {
    private Integer businessValue;
    private Integer urgency;
    private Integer riskReduction;
    private Integer effort;
}
