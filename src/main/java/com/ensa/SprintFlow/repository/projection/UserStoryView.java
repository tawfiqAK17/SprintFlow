package com.ensa.SprintFlow.repository.projection;

public record UserStoryView(Long id, String title, Integer priority, EpicView epicView, SprintView sprintView) {}
