package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryViewDto;
import com.ensa.SprintFlow.enums.PrioritizationType;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner;
import com.ensa.SprintFlow.service.UserStoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@AllArgsConstructor
public class UserStoryController {
    private UserStoryService userStoryService;

    @AuthorizeMember
    @GetMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> getUserStories(@PathVariable("project_id") Long projectId,
                                            @RequestParam(value = "epic_id", required = false) Long epicId,
                                            @RequestParam(value = "sprint_id",required = false) Long sprintId,
                                            @RequestParam(value = "unassigned", defaultValue = "false", required = false) Boolean unassignedOnly,
                                            @RequestParam(value = "prioritizationType", defaultValue = "MoSCoW", required = false) PrioritizationType prioritizationType) {
        List<UserStoryViewDto> userStoryViewDtoList = userStoryService.getUserStories( projectId, epicId, sprintId, unassignedOnly, prioritizationType);
        if(userStoryViewDtoList.isEmpty()){
            return ResponseEntity.status( HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status( HttpStatus.OK).body( userStoryViewDtoList);
    }

    @AuthorizeMember
    @GetMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> getUserStory(@PathVariable("project_id") Long projectId,
                                          @PathVariable("id") Long userStoryId,
                                          @RequestParam(value = "prioritizationType", defaultValue = "MoSCoW", required = false) PrioritizationType prioritizationType){
        UserStoryResponseDto userStory = userStoryService.getUserStory( userStoryId, prioritizationType);
        return ResponseEntity.status(HttpStatus.OK).body( userStory);
    }

    @AuthorizeProductOwner
    @PostMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> createUserStory(@PathVariable("project_id") Long projectId,
                                             @Validated @RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.createUserStory( projectId, userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @AuthorizeProductOwner
    @PatchMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> updateUserStory(@PathVariable("project_id") Long projectId,
                                             @PathVariable("id") Long userStoryId,
                                             @RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.updateUserStory( userStoryId, userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthorizeProductOwner
    @DeleteMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> deleteUserStory(@PathVariable("project_id") Long projectId,
                                             @PathVariable("id") Long userStoryId){
        userStoryService.deleteUserStory( userStoryId);
        return ResponseEntity.status( HttpStatus.OK).build();
    }
}
