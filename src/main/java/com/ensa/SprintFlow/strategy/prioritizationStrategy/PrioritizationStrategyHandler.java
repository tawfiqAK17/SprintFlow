package com.ensa.SprintFlow.strategy.prioritizationStrategy;

import com.ensa.SprintFlow.enums.PrioritizationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PrioritizationStrategyHandler {
    private final Map<PrioritizationType, PrioritizationStrategy> strategyMap;

    @Autowired
    public PrioritizationStrategyHandler(List<PrioritizationStrategy> strategyList) {
        strategyMap = new EnumMap<>( PrioritizationType.class);
        for( PrioritizationStrategy strategy : strategyList){
            if (strategyMap.containsKey( strategy.getStrategyType())){
                throw new RuntimeException("Fatal dependency injection error: multiple strategy instances found for the same type.");
            }
            strategyMap.put( strategy.getStrategyType(), strategy);
        }
    }

    public PrioritizationStrategy getStrategy(PrioritizationType type){
        if( !strategyMap.containsKey( type)){
            throw new RuntimeException(
                    String.format("No prioritization strategy implementation found for the requested type %s.", type)
            );
        }
        return strategyMap.get( type);
    }
}
