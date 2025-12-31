package com.ensa.SprintFlow.dto.userStoryDescription;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserStoryDescriptionDto {
    private String asWho;
    private String what;
    private String forDesc;
}
