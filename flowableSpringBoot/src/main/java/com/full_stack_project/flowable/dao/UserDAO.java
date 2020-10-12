package com.full_stack_project.flowable.dao;

import com.full_stack_project.flowable.modal.User;

import java.util.List;

public interface UserDAO {
    List<User> get();
    User get(String id);
}
