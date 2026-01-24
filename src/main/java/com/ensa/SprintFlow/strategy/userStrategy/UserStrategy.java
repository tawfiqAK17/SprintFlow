package com.ensa.SprintFlow.strategy.userStrategy;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.model.Report;

import java.util.Set;

public interface UserStrategy {
    Role getRole();
    Set<TaskStatus> getAllowedTaskStatuses();
    default boolean canSetTaskStatus(TaskStatus status){
        return getAllowedTaskStatuses().contains( status);
    };
}
