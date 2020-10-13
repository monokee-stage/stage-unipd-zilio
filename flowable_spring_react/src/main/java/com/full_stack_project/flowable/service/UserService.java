package com.full_stack_project.flowable.service;

import com.full_stack_project.flowable.modal.User;

import java.util.List;

public interface UserService {
    List<User> get();
    User get(String id);
}
