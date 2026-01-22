package com.ensa.SprintFlow.strategy.prioritizationStrategy.impl;

import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.enums.PriorityLabel;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryPriority;
import com.ensa.SprintFlow.strategy.prioritizationStrategy.PrioritizationStrategy;
import org.springframework.stereotype.Component;

@Component
public class ValueVsEffortStrategy implements PrioritizationStrategy {
    @Override
    public PrioritizationType getStrategyType() {
        return PrioritizationType.ValueVsEffort;
    }

    @Override
    public double calculateUserStoryScore(Integer businessValue, Integer urgency, Integer riskReduction, Integer effort) {
        return 0;
    }

    @Override
    public PriorityLabel classifyUserStory(double score) {
        return null;
    }

    @Override
    public UserStoryPriority getUserStoryPriority(UserStory userStory) {
        return null;
    }
}
