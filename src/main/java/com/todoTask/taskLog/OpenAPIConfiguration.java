package com.todoTask.taskLog;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("development")
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineAccount() {
        Server server = new Server();
        server.setUrl("http://localhost:8080/");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Feivel Doctora");
        myContact.setEmail("fvdoctorawork@gmail.com");

        Info information = new Info()
                .title("Task Log Documentation")
                .version("1.0")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));

    }
}
