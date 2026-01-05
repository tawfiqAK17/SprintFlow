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
            ands.add( And.builder()._and( and_expression ).build());
        }
        
        return AcceptanceCriteria.builder()
                ._given( dto.getGiven())
                ._when( dto.getWhen())
                .ands( ands)
                ._then(dto.getThen())
                .build();
    }
    
    public AcceptanceCriteriaDto mapToAcceptanceCriteriaDto( AcceptanceCriteria acceptanceCriteria){
        List<String> ands = new ArrayList<>();
        for( And and_expression : acceptanceCriteria.getAnds()){
            ands.add( and_expression.get_and());
        }
        
        return  AcceptanceCriteriaDto.builder()
                .given( acceptanceCriteria.get_given())
                .when( acceptanceCriteria.get_when())
                .ands( ands)
                .then( acceptanceCriteria.get_then())
                .build();
    }
}
