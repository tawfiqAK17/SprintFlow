package com.ensa.SprintFlow.service.strategy;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.service.strategy.impl.CompositeTransitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TaskTransitionStrategyHandler {
    private final Map<Role, TaskTransitionStrategy> taskStrategyMap;

    @Autowired
    public TaskTransitionStrategyHandler(List<TaskTransitionStrategy> strategies){
        taskStrategyMap = new HashMap<>();
        for( TaskTransitionStrategy strategy : strategies){
            if (taskStrategyMap.containsKey( strategy.getRole())){
                // we should throw an exception (DI problem as there is two instance
                // of TaskStrategy with the same Role)
            }
            taskStrategyMap.put( strategy.getRole(), strategy);
        }
    }

    public TaskTransitionStrategy getStrategy( List<Role> roles){
        TaskTransitionStrategy strategy = null;
        if( roles.size() == 1){
            if( !taskStrategyMap.containsKey( roles.getFirst())){
                // return an exception
            }
            strategy = taskStrategyMap.get( roles.getFirst());
        }
        else if( roles.size() > 1){
            List<TaskTransitionStrategy> strategies = new ArrayList<>();
            for( Role role : roles){
                if( !taskStrategyMap.containsKey( role)){
                    // return an exception
                }
                strategies.add( taskStrategyMap.get( role));
            }
            strategy = new CompositeTransitionStrategy( strategies);
        }
        if (strategy == null){
            // Throw an exception ....
        }
        return strategy;
    }
}
