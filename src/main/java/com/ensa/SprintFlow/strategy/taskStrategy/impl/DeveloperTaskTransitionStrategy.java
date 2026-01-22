package com.ensa.SprintFlow.strategy.taskStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.taskStrategy.TaskTransitionStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class DeveloperTaskTransitionStrategy implements TaskTransitionStrategy {
    @Override
    public Role getRole(){
        return Role.DEVELOPER;
    }

    @Override
    public Set<TaskStatus> getAllowedStatus(){
        return EnumSet.of( TaskStatus.IN_PROGRESS, TaskStatus.TO_BE_TESTED);
    }
}
