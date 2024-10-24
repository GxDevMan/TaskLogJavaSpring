package com.todoTask.taskLog.repository;
import com.todoTask.taskLog.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByuserName(String user_name);
}
