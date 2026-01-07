package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.model.AcceptanceCriteria;
import com.ensa.SprintFlow.model.And;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AcceptanceCriteriaMapper {
    
    public AcceptanceCriteria mapToAcceptanceCriteria(AcceptanceCriteriaDto dto){
        Set<And> ands = new HashSet<>();
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
        Set<String> ands = new HashSet<>();
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

    public List<AcceptanceCriteriaDto> mapToAcceptanceCriteriaDto( List<AcceptanceCriteria> criteriaList){
        List<AcceptanceCriteriaDto> criteriaDtoList = new ArrayList<>();
        for(AcceptanceCriteria criteria : criteriaList){
            criteriaDtoList.add( mapToAcceptanceCriteriaDto( criteria));
        }
        return criteriaDtoList;
    }
}
