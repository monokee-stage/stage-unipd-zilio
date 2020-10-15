package com.full_stack_project.flowable.controller;


import com.full_stack_project.flowable.modal.Admin;
import com.full_stack_project.flowable.repository.AdminRepository;
import com.full_stack_project.flowable.repository.GetLastAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private GetLastAdminRepository getLastAdminRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/getLastAdmin")
    public Admin getLastAdmin() {
        return getLastAdminRepository.findLastAdmin();
    }

    @PostMapping("/insertAdmin/{username}")
    public void insertAdmin(@PathVariable String username) throws Exception {
        adminRepository.insertAdmin(new Admin(username));
    }

}
