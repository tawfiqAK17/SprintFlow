package com.ensa.SprintFlow.service.strategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.service.strategy.TaskTransitionStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class ScrumMasterTaskTransitionStrategy implements TaskTransitionStrategy {
    @Override
    public Role getRole(){
        return Role.SCRUM_MASTER;
    }

    @Override
    public Set<TaskStatus> getAllowedStatus(){
        return EnumSet.allOf( TaskStatus.class);
    }

}