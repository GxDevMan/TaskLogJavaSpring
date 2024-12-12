package com.todoTask.taskLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TaskLogApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();

			System.setProperty("DB_URL", System.getProperty("DB_URL", dotenv.get("DB_URL")));
			System.setProperty("DB_USERNAME", System.getProperty("DB_USERNAME", dotenv.get("DB_USERNAME")));
			System.setProperty("DB_PASSWORD", System.getProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD")));
			System.setProperty("MAXIMUM_POOL_SIZE", System.getProperty("MAXIMUM_POOL_SIZE", dotenv.get("MAXIMUM_POOL_SIZE")));
			System.setProperty("MINIMUM_IDLE", System.getProperty("MINIMUM_IDLE", dotenv.get("MINIMUM_IDLE")));
			System.setProperty("IDLE_TIMEOUT", System.getProperty("IDLE_TIMEOUT", dotenv.get("IDLE_TIMEOUT")));
			System.setProperty("MAX_LIFETIME", System.getProperty("MAX_LIFETIME", dotenv.get("MAX_LIFETIME")));
			System.setProperty("ADMIN_USERNAME", System.getProperty("ADMIN_USERNAME", dotenv.get("ADMIN_USERNAME")));
			System.setProperty("ADMIN_PASSWORD", System.getProperty("ADMIN_PASSWORD", dotenv.get("ADMIN_PASSWORD")));
		} catch (Exception e){

		}
		SpringApplication.run(TaskLogApplication.class, args);
	}

}
