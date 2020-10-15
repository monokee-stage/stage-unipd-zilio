package com.full_stack_project.flowable.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACT_ADMINS")
public class Admin {
    @Id
    @Column(name = "username")
    private String username;

    public Admin() {}

    public Admin(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
