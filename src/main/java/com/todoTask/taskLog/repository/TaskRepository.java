package com.todoTask.taskLog.repository;
import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByuserAccount(UserAccount userAccount);
}
