package org.benrush.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "org.benrush.server.persistence.mapper")
@EnableTransactionManagement
public class ProjectMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMainApplication.class, args);
    }
}
