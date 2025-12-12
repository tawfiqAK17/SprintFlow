package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByUsername(String username);

  public User findByEmail(String email);

  public boolean existsByUsername(String username);

  public boolean existsByEmail(String email);
}
