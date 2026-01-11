package com.ensa.SprintFlow.service.strategy;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;


import java.util.Set;

public interface TaskTransitionStrategy {
    Role getRole();
    Set<TaskStatus> getAllowedStatus();

    private boolean validateTransition(TaskStatus currentStatus, TaskStatus newStatus){
        return currentStatus == newStatus || currentStatus.getValue() < newStatus.getValue()
                || (currentStatus == TaskStatus.TEST_FAILED && newStatus == TaskStatus.IN_PROGRESS);
    }

    default void validateStatus(TaskStatus currentStatus, TaskStatus newStatus){
        // 1. Is the status in the allowed Set?
        boolean isAllowedStatus = getAllowedStatus().contains(newStatus);

        // 2. Is the movement valid (logical order)?
        boolean isValidMove = validateTransition(currentStatus, newStatus);

        if( !isAllowedStatus || !isValidMove){
            throw new UnauthorizedException("You can't mark this task as "+ newStatus.toString());
        }
    }
}
