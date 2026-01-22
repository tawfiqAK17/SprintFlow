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
            strategyMap.put( strategy.getStrategyType(), strategy);
        }
    }

    public PrioritizationStrategy getStrategy(PrioritizationType type){
        if( !strategyMap.containsKey( type)){
            return null;   // return default
        }
        return strategyMap.get( type);
    }
}
