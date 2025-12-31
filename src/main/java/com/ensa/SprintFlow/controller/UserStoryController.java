package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.builder.ResponseBuilder;
import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
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
    private ResponseBuilder responseBuilder;
    
    @GetMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> getUserStories() {
        List<UserStoryMetaDataResponseDto> userStories = userStoryService.getUserStories();
        return responseBuilder.status(HttpStatus.OK).property("user_stories", userStories).build();
    }
    
    @GetMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> getUserStory(@PathVariable Long id){
        UserStoryResponseDto userStory = userStoryService.getUserStory( id);
        return ResponseEntity.status(HttpStatus.OK).body( userStory);
    }

    @PostMapping("/projects/{project_id}/user_stories")
    public ResponseEntity<?> createUserStory(@Validated @RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.createUserStory( userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> updateUserStory(@PathVariable Long id ,@RequestBody UserStoryRequestDto userStoryRequestDto){
        userStoryService.updateUserStory( id, userStoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/projects/{project_id}/user_stories/{id}")
    public ResponseEntity<?> deleteUserStory(@PathVariable Long id){
        userStoryService.deleteUserStory( id);
        return ResponseEntity.status( HttpStatus.OK).build();
    }
}
