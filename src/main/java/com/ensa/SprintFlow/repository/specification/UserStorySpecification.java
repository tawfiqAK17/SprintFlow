package com.ensa.SprintFlow.repository.specification;

import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.UserStory;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserStorySpecification {
    public static Specification<UserStory> whereProject(Long projectId){
        return (root, query, criteriaBuilder) -> {
            Join<UserStory, Epic> epic = root.join( "epic");
            Join<Epic, Project> project = epic.join( "project");
            return criteriaBuilder.equal( project.get("id"), projectId);
        };
    }

    public static Specification<UserStory> belongsToSprint( Long sprintId){
        return (root, query, criteriaBuilder) -> {
            Join<UserStory, Sprint> sprint = root.join("sprint");
            return criteriaBuilder.equal( sprint.get("id"), sprintId);
        };
    }

    public static Specification<UserStory> belongsToEpic( Long epicId){
        return (root, query, criteriaBuilder) -> {
            Join<UserStory, Epic> epic = root.join("epic");
            return criteriaBuilder.equal( epic.get("id"), epicId);
        };
    }

    public static Specification<UserStory> unassignedOnly(){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal( root.get("sprint"), null);
        };
    }
}
