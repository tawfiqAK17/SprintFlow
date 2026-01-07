package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.mapper.AcceptanceCriteriaMapper;
import com.ensa.SprintFlow.model.AcceptanceCriteria;
import com.ensa.SprintFlow.model.And;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.AcceptanceCriteriaRepository;
import com.ensa.SprintFlow.repository.UserStoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@AllArgsConstructor
public class AcceptanceCriteriaService {
    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;
    private UserStoryRepository userStoryRepository;
    private AcceptanceCriteriaMapper mapper;

    public void createAcceptanceCriteria(Long userStoryId, AcceptanceCriteriaDto acceptanceCriteriaDto){
        AcceptanceCriteria acceptanceCriteria = mapper.mapToAcceptanceCriteria( acceptanceCriteriaDto);
        UserStory userStory = userStoryRepository.findById(userStoryId).orElseThrow();
        acceptanceCriteria.setUserStory( userStory);

        for(And and : acceptanceCriteria.getAnds()){
            and.setAcceptanceCriteria( acceptanceCriteria);
        }

        acceptanceCriteriaRepository.save( acceptanceCriteria);
    }

    @Transactional
    public void updateAcceptanceCriteria(Long criteriaId, AcceptanceCriteriaDto acceptanceCriteriaDto){
        AcceptanceCriteria criteriaFromDto = mapper.mapToAcceptanceCriteria( acceptanceCriteriaDto);
        AcceptanceCriteria acceptanceCriteria = acceptanceCriteriaRepository.findById(criteriaId).orElseThrow();
        if( criteriaFromDto.getGivenWhat() != null){
            acceptanceCriteria.setGivenWhat( criteriaFromDto.getGivenWhat());
        }

        if ( criteriaFromDto.getWhenWhat() != null){
            acceptanceCriteria.setWhenWhat( criteriaFromDto.getWhenWhat());
        }

        if (criteriaFromDto.getThenWhat() != null){
            acceptanceCriteria.setThenWhat(criteriaFromDto.getThenWhat());
        }

        if ( criteriaFromDto.getAnds() != null){
            // we remove all old 'and', those aren't anymore in the coming request dto, from 'ands' table
            // this time, we remove from dto all 'and' that we have already in 'ands' table and they didn't change
            Iterator<And> iterator = acceptanceCriteria.getAnds().iterator();
            while ( iterator.hasNext()){
                And and = iterator.next();
                if ( !criteriaFromDto.getAnds().contains( and) ){
                    iterator.remove();
                }
                else{
                    criteriaFromDto.getAnds().remove( and);
                }
            }
            // we add all remaining 'ands' to 'ands' table ( both new ones and updated ones)
            for( And and : criteriaFromDto.getAnds()){
                and.setAcceptanceCriteria( acceptanceCriteria);
                acceptanceCriteria.getAnds().add( and);
            }
        }
    }

    public void deleteAcceptanceCriteria(Long criteria_id){
        acceptanceCriteriaRepository.deleteById( criteria_id);
    }
}

