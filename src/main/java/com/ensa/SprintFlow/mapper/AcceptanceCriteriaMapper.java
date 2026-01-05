package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.model.AcceptanceCriteria;
import com.ensa.SprintFlow.model.And;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AcceptanceCriteriaMapper {
    
    public AcceptanceCriteria mapToAcceptanceCriteria(AcceptanceCriteriaDto dto){
        List<And> ands = new ArrayList<>();
        for( String and_expression : dto.getAnds()){
            ands.add( And.builder().andWhat( and_expression ).build());
        }
        
        return AcceptanceCriteria.builder()
                .givenWhat( dto.getGiven())
                .whenWhat( dto.getWhen())
                .ands( ands)
                .thenWhat(dto.getThen())
                .build();
    }
    
    public AcceptanceCriteriaDto mapToAcceptanceCriteriaDto( AcceptanceCriteria acceptanceCriteria){
        List<String> ands = new ArrayList<>();
        for( And and_expression : acceptanceCriteria.getAnds()){
            ands.add( and_expression.getAndWhat());
        }
        
        return  AcceptanceCriteriaDto.builder()
                .given( acceptanceCriteria.getGivenWhat())
                .when( acceptanceCriteria.getWhenWhat())
                .ands( ands)
                .then( acceptanceCriteria.getThenWhat())
                .build();
    }
}
