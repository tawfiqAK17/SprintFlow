package com.ensa.SprintFlow.strategy.prioritizationStrategy;

import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.enums.PriorityLabel;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryPriority;

import java.util.List;
import java.util.stream.Collectors;

public interface PrioritizationStrategy {
    PrioritizationType getStrategyType();

    double calculateUserStoryScore( Integer businessValue, Integer urgency,Integer riskReduction,Integer effort);

    PriorityLabel classifyUserStory(double score);

    UserStoryPriority getUserStoryPriority(UserStory userStory);

    default List<UserStoryPriority> getUserStoryPriority( List<UserStory> userStories){
        return userStories.stream().map(userStory -> getUserStoryPriority( userStory))
                .collect(Collectors.toList());
    }



}
