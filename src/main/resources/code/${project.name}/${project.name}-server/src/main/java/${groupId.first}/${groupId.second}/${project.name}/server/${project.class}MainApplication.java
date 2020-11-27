package ${project.groupId}.${project.name}.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "${project.groupId}.${project.name}.server.persistence.mapper")
@EnableTransactionManagement
public class ${project.class}MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(${project.class}MainApplication.class, args);
    }
}
