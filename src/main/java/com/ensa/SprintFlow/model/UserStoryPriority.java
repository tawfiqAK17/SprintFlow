package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.PriorityLabel;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryPriority {
    private double score;
    private PriorityLabel label;
}
