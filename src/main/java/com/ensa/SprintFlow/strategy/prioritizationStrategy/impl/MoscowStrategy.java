package com.ensa.SprintFlow.strategy.prioritizationStrategy.impl;

import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.enums.PriorityLabel;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryMetrics;
import com.ensa.SprintFlow.model.UserStoryPriority;
import com.ensa.SprintFlow.strategy.prioritizationStrategy.PrioritizationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MoscowStrategy implements PrioritizationStrategy {

    @Override
    public PrioritizationType getStrategyType() {
        return PrioritizationType.MoSCoW;
    }

    @Override
    public double calculateUserStoryScore(Integer businessValue, Integer urgency, Integer riskReduction, Integer effort) {
        // Moscow strategy focus just on businessValue and urgency with a higher coefficient of urgency
        return businessValue + urgency * 2;
    }

    @Override
    public PriorityLabel classifyUserStory(double score) {
        if( score >= 40){
            return PriorityLabel.DO_NOW;
        }else if( score >= 25){
            return PriorityLabel.DO_NEXT;
        }else if( score >= 12){
            return PriorityLabel.DO_LATER;
        }
        return PriorityLabel.DONT_DO;
    }

    @Override
    public UserStoryPriority getUserStoryPriority(UserStory userStory) {
        UserStoryMetrics metrics = userStory.getMetrics();
        if(metrics ==null){
            return null;
        }
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
