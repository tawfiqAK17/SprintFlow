package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeScrumMaster;
import com.ensa.SprintFlow.service.AcceptanceCriteriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class AcceptanceCriteriaController {
    private AcceptanceCriteriaService acceptanceCriteriaService;

    @AuthorizeScrumMaster
    @PostMapping("/projects/{project_id}/user_stories/{user_story_id}/criterias")
    ResponseEntity<?> createUserStory(@PathVariable("project_id") Long projectId,
                                      @PathVariable("user_story_id")Long userStoryId,
                                      @Validated @RequestBody AcceptanceCriteriaDto acceptanceCriteriaDto){
        acceptanceCriteriaService.createAcceptanceCriteria( userStoryId ,acceptanceCriteriaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @AuthorizeScrumMaster
    @PutMapping("/projects/{project_id}/user_stories/{user_story_id}/criterias/{criteria_id}")
    ResponseEntity<?> createUserStory(@PathVariable("project_id") Long projectId,
                                      @PathVariable("user_story_id")Long userStoryId,
                                      @PathVariable("criteria_id") Long criteriaId,
                                      @RequestBody AcceptanceCriteriaDto acceptanceCriteriaDto){
        acceptanceCriteriaService.updateAcceptanceCriteria( criteriaId, acceptanceCriteriaDto);
        return ResponseEntity.status( HttpStatus.OK).build();
    }

    @AuthorizeScrumMaster
    @DeleteMapping("/projects/{project_id}/user_stories/{user_story_id}/criterias/{criteria_id}")
    ResponseEntity<?> createUserStory(@PathVariable("project_id") Long projectId,
                                      @PathVariable("user_story_id")Long userStoryId,
                                      @PathVariable("criteria_id") Long criteriaId){
        acceptanceCriteriaService.deleteAcceptanceCriteria( criteriaId);
        return ResponseEntity.status( HttpStatus.NO_CONTENT).build();
    }
}
