package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
