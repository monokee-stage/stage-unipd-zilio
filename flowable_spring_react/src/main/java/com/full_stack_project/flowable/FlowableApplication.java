package com.full_stack_project.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class FlowableApplication {

    public static void main(String[] args) {

        SpringApplication.run(FlowableApplication.class, args);
    }

}
