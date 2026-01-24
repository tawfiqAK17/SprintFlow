package com.ensa.SprintFlow.strategy.userStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class TesterStrategy implements UserStrategy {
    @Override
    public Role getRole(){
        return Role.TESTER;
    }

    @Override
    public Set<TaskStatus> getAllowedTaskStatuses(){
        return EnumSet.of( TaskStatus.TEST_FAILED, TaskStatus.TESTED);
    }
}