package com.ensa.SprintFlow.strategy.prioritizationStrategy.impl;

import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.enums.PriorityLabel;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryMetrics;
import com.ensa.SprintFlow.model.UserStoryPriority;
import com.ensa.SprintFlow.strategy.prioritizationStrategy.PrioritizationStrategy;
import org.springframework.stereotype.Component;

@Component
public class WsjfStrategy implements PrioritizationStrategy {
    @Override
    public PrioritizationType getStrategyType() {
        return PrioritizationType.WSJF;
    }

    @Override
    public double calculateUserStoryScore(Integer businessValue, Integer urgency, Integer riskReduction, Integer effort) {
        effort = (effort == 0 ? 1 : effort);
        return (double) (businessValue + urgency + riskReduction) / effort;
    }

    @Override
    public PriorityLabel classifyUserStory(double score) {
        if( score >= 13d){
            return PriorityLabel.DO_NOW;
        }else if( score >= 5d){
            return PriorityLabel.DO_NEXT;
        }else if( score >= 1.5d){
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
