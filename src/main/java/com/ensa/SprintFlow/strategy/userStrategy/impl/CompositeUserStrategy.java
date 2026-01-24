package com.ensa.SprintFlow.strategy.userStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategy;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CompositeUserStrategy implements UserStrategy {
        private final Set<TaskStatus> allowedStatus;

        public CompositeUserStrategy(List<UserStrategy> strategies){
            this.allowedStatus = EnumSet.noneOf( TaskStatus.class);
            for (UserStrategy strategy : strategies) {
                allowedStatus.addAll( strategy.getAllowedTaskStatuses());
            }
        }

        @Override
        public Role getRole(){
            // what really matter is allowed status not the role itself,
            // so we can just return a null value
            return null;
        }

        @Override
        public Set<TaskStatus> getAllowedTaskStatuses(){
            return allowedStatus;
        }
}
