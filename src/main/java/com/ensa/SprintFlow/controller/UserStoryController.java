package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.service.UserStoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController()
@AllArgsConstructor
public class UserStoryController {
    private UserStoryService userStoryService;
    
    @GetMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> getUserStories(@PathVariable("project_id") Long projectId) {
        return ResponseEntity.status( HttpStatus.OK).body( userStoryService.getUserStories());
    }
    
    @GetMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> getUserStory(@PathVariable("project_id") Long projectId,
                                          @PathVariable("id") Long userStoryId){
        UserStoryResponseDto userStory = userStoryService.getUserStory( userStoryId);
        return ResponseEntity.status(HttpStatus.OK).body( userStory);
    }

    @PostMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> createUserStory(@PathVariable("project_id") Long projectId,
                                             @Validated @RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.createUserStory( userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> updateUserStory(@PathVariable("project_id") Long projectId,
                                             @PathVariable("id") Long userStoryId,
                                             @RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.updateUserStory( userStoryId, userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> deleteUserStory(@PathVariable("project_id") Long projectId,
                                             @PathVariable("id") Long userStoryId){
        userStoryService.deleteUserStory( userStoryId);
        return ResponseEntity.status( HttpStatus.OK).build();
    }
}
