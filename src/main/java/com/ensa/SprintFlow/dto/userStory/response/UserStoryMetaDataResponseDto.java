package com.ensa.SprintFlow.dto.userStory.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class UserStoryMetaDataResponseDto {
    private Long id;
    private String title;
}
