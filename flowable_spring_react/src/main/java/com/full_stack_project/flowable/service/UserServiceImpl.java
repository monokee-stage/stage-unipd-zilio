package com.full_stack_project.flowable.service;

import com.full_stack_project.flowable.modal.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<User> get() {
        Session currSession = entityManager.unwrap(Session.class);
        Query<User> query = currSession.createQuery("", User.class);
        List<User> list = query.getResultList();
        return list;
    }

    @Override
    public User get(String id) {
        Session currSession = entityManager.unwrap(Session.class);
        User user = currSession.get(User.class, id);
        return user;
    }
}