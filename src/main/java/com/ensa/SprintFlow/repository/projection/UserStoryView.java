package com.ensa.SprintFlow.repository.projection;

public record UserStoryView(Long id, String title, Integer priority, EpicView epic, SprintView sprint) {

    public UserStoryView(Long id, String title, Integer priority, Long epicId,
                         String epicTitle, Long sprintId, String sprintTitle) {
        this(
            id,
            title,
            priority,
            (epicId == null) ? null : new EpicView(epicId, epicTitle),
            (sprintId == null) ? null : new SprintView(sprintId, sprintTitle)
        );
    }
}
