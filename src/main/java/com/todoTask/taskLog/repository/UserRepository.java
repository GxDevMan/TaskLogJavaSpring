package com.todoTask.taskLog.repository;
import com.todoTask.taskLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByuserName(String user_name);
}
