package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class TaskTransitionService {
    private boolean canTransition(TaskStatus currentStatus, TaskStatus newStatus){
        return currentStatus == newStatus || currentStatus.getValue() < newStatus.getValue()
                || (currentStatus == TaskStatus.TEST_FAILED && newStatus == TaskStatus.IN_PROGRESS);
    }

    private boolean canUserSetTaskStatus( UserStrategy userStrategy, TaskStatus newStatus){
        return userStrategy.canSetTaskStatus( newStatus);
    }

    void validateTaskStatus(UserStrategy userStrategy, TaskStatus currentStatus, TaskStatus newStatus){
        // 1. can user set TaskStatus
        boolean isUserAllowed = canUserSetTaskStatus( userStrategy, newStatus);

        // 2. Is the movement valid (logical order)
        boolean isValidMove = canTransition(currentStatus, newStatus);

        if( !isUserAllowed || !isValidMove){
            throw new UnauthorizedException("You can't mark this task as "+ newStatus.toString());
        }
    }
}
