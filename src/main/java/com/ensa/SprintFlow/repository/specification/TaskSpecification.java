package com.ensa.SprintFlow.repository.specification;

import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.Task;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.UserStory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TaskSpecification {
    public static Specification<Task> emptyWhere(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
    public static Specification<Task> belongsToSprint( Long sprintId){
        return (root, query, criteriaBuilder) -> {
            Join<Task, UserStory> userStory = root.join("userStory");
            Join<UserStory, Sprint> sprint = userStory.join("sprint");
            return criteriaBuilder.equal( sprint.get("id"), sprintId);
        };
    }

    public static Specification<Task> belongsToUserStory( Long userStoryId){
        return (root, query, criteriaBuilder) -> {
            Join<Task, UserStory> userStory = root.join("userStory");
            return criteriaBuilder.equal( userStory.get("id"), userStoryId);
        };
    }

    public static Specification<Task> hasStatus(List<TaskStatus> statusList){
        return (root, query, criteriaBuilder) -> {
            return root.get("status").in(statusList);
        };
    }

    public static Specification<Task>  assignedToDeveloper(String developerUserName){
        return (root, query, criteriaBuilder) -> {
            Join<Task,User> developer = root.join("developer");
            return criteriaBuilder.equal( developer.get("username"), developerUserName);
        };
    }

    public static Specification<Task>  assignedToTester(String testerUserName){
        return (root, query, criteriaBuilder) -> {
            Join<Task,User> developer = root.join("tester");
            return criteriaBuilder.equal( developer.get("username"), testerUserName);
        };
    }
}
