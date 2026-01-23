package com.ensa.SprintFlow.strategy.taskStrategy;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.strategy.taskStrategy.impl.CompositeTransitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TaskTransitionStrategyHandler {
    private final Map<Role, TaskTransitionStrategy> taskStrategyMap;

    @Autowired
    public TaskTransitionStrategyHandler(List<TaskTransitionStrategy> strategies){
        taskStrategyMap = new EnumMap<>( Role.class);
        for( TaskTransitionStrategy strategy : strategies){
            if (taskStrategyMap.containsKey( strategy.getRole())){
                throw new RuntimeException("Fatal dependency injection error: multiple strategy instances found for the same role.");
            }
            taskStrategyMap.put( strategy.getRole(), strategy);
        }
    }

    public TaskTransitionStrategy getStrategy( List<Role> roles){
        TaskTransitionStrategy strategy = null;
        if( roles.size() == 1){
            if( !taskStrategyMap.containsKey( roles.getFirst())){
                throw new RuntimeException(
                        String.format("No user strategy implementation found for the requested role %s.", roles.getFirst())
                );
            }
            strategy = taskStrategyMap.get( roles.getFirst());
        }
        else if( roles.size() > 1){
            List<TaskTransitionStrategy> strategies = new ArrayList<>();
            for( Role role : roles){
                if( !taskStrategyMap.containsKey( role)){
                    throw new RuntimeException(
                            String.format("No user strategy implementation found for the requested role %s.", roles.getFirst())
                    );
                }
                strategies.add( taskStrategyMap.get( role));
            }
            strategy = new CompositeTransitionStrategy( strategies);
        }
        if (strategy == null){
            throw new RuntimeException("User strategy composition failed.");
        }
        return strategy;
    }
}
