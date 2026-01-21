package com.ensa.SprintFlow.strategy.taskStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.taskStrategy.TaskTransitionStrategy;

import java.util.*;

public class CompositeTransitionStrategy implements TaskTransitionStrategy {
        private final Set<TaskStatus> allowedStatus;

        public CompositeTransitionStrategy( List<TaskTransitionStrategy> strategies){
            this.allowedStatus = EnumSet.noneOf( TaskStatus.class);
            for (TaskTransitionStrategy strategy : strategies) {
                allowedStatus.addAll( strategy.getAllowedStatus());
            }
        }

        @Override
        public Role getRole(){
            // what really matter is allowed status not the role itself,
            // so we can just return a null value
            return null;
        }

        @Override
        public Set<TaskStatus> getAllowedStatus(){
            return allowedStatus;
        }
}
