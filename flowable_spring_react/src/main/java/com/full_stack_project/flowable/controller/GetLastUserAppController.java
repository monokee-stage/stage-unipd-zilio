package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.GetLastUserApp;
import com.full_stack_project.flowable.repository.AddAppValidatedRepository;
import com.full_stack_project.flowable.repository.GetLastUserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class GetLastUserAppController {

    @Autowired
    private GetLastUserAppRepository getLastUserAppRepository;

    @GetMapping("/getLastUserApp")
    public GetLastUserApp getLast(){
        return getLastUserAppRepository.findLast();
    }
}
