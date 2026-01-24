package com.ensa.SprintFlow.strategy.prioritizationStrategy.impl;

import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.enums.PriorityLabel;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryMetrics;
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
        effort = (effort == 0 ? 1 : effort);
        return (double) businessValue/effort;
    }

    @Override
    public PriorityLabel classifyUserStory(double score) {
        if( score >= 5d){
            return PriorityLabel.DO_NOW;
        }else if( score >= 2){
            return PriorityLabel.DO_NEXT;
        }else if( score >= 0.5){
            return PriorityLabel.DO_LATER;
        }
        return PriorityLabel.DONT_DO;
    }

    @Override
    public UserStoryPriority getUserStoryPriority(UserStory userStory) {
        UserStoryMetrics metrics = userStory.getMetrics();
        double score = calculateUserStoryScore(
                metrics.getBusinessValue(),
                metrics.getUrgency(),
                metrics.getRiskReduction(),
                metrics.getEffort()
        );
        PriorityLabel label = classifyUserStory( score);

        return UserStoryPriority.builder().score( score).label( label).build();
    }
}
