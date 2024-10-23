package com.todoTask.taskLog.repository;
import com.todoTask.taskLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUser_Name(String user_name);
}
