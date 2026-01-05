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
        AcceptanceCriteria newAcceptanceCriteria = mapper.mapToAcceptanceCriteria( acceptanceCriteriaDto);
        AcceptanceCriteria acceptanceCriteria = acceptanceCriteriaRepository.findById(criteriaId).orElseThrow();
        if( newAcceptanceCriteria.getGivenWhat() != null){
            acceptanceCriteria.setGivenWhat( newAcceptanceCriteria.getGivenWhat());
        }

        if ( newAcceptanceCriteria.getWhenWhat() != null){
            acceptanceCriteria.setWhenWhat( newAcceptanceCriteria.getWhenWhat());
        }

        if (newAcceptanceCriteria.getThenWhat() != null){
            acceptanceCriteria.setThenWhat(newAcceptanceCriteria.getThenWhat());
        }

        if ( newAcceptanceCriteria.getAnds() != null){
            for(And and : newAcceptanceCriteria.getAnds()){
                and.setAcceptanceCriteria( acceptanceCriteria);
            }
            acceptanceCriteria.setAnds( newAcceptanceCriteria.getAnds());
        }
    }

    public void deleteAcceptanceCriteria(Long criteria_id){
        acceptanceCriteriaRepository.deleteById( criteria_id);
    }
}

