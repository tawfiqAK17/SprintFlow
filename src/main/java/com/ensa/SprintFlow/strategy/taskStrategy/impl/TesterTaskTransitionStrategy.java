package com.ensa.SprintFlow.strategy.taskStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.taskStrategy.TaskTransitionStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class TesterTaskTransitionStrategy implements TaskTransitionStrategy {
    @Override
    public Role getRole(){
        return Role.TESTER;
    }

    @Override
    public Set<TaskStatus> getAllowedStatus(){
        return EnumSet.of( TaskStatus.TEST_FAILED, TaskStatus.TESTED);
    }

}