package com.ensa.SprintFlow.strategy.userStrategy.impl;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategy;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductOwnerStrategy implements UserStrategy {
    @Override
    public Role getRole() {
        return Role.PRODUCT_OWNER;
    }

    @Override
    public Set<TaskStatus> getAllowedTaskStatuses() {
        return Set.of();
    }
}
