package com.ensa.SprintFlow.strategy.userStrategy;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.strategy.userStrategy.impl.CompositeUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Component
public class UserStrategyHandler {
    private final Map<Role, UserStrategy> strategyMap;

    @Autowired
    public UserStrategyHandler(List<UserStrategy> strategies){
        strategyMap = new EnumMap<>( Role.class);
        for( UserStrategy strategy : strategies){
            if(strategyMap.containsKey( strategy.getRole())){
                throw new RuntimeException("Fatal dependency injection error: multiple user strategy instances found for the same role.");
            }
            strategyMap.put( strategy.getRole(), strategy);
        }
    }

    public UserStrategy getStrategy( List<Role> roles){
        UserStrategy strategy = null;
        if( roles.size() == 1){
            if( !strategyMap.containsKey( roles.getFirst())){
                throw new RuntimeException(
                        String.format("No user strategy implementation found for the requested role %s.", roles.getFirst())
                );
            }
            strategy = strategyMap.get( roles.getFirst());
        }
        else if( roles.size() > 1){
            List<UserStrategy> strategies = new ArrayList<>();
            for( Role role : roles){
                if( !strategyMap.containsKey( role)){
                    throw new RuntimeException(
                            String.format("No user strategy implementation found for the requested role %s.", roles.getFirst())
                    );
                }
                strategies.add( strategyMap.get( role));
            }
            strategy = new CompositeUserStrategy( strategies);
        }
        if (strategy == null){
            throw new RuntimeException("User strategy composition failed.");
        }
        return strategy;
    }
}
