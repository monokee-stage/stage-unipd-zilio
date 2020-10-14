package com.full_stack_project.flowable.modal;

import javax.persistence.*;

@Entity
@Table(name = "ACT_ID_USER_APP_VALIDATED")
public class AddAppValidated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "username")
    private String user;
    @Column(name = "app_name")
    private String value;
    @Column(name = "validator")
    private String validator;

    public AddAppValidated(){}

    public AddAppValidated(String user, String value, String validator) {
        this.user = user;
        this.value = value;
        this.validator = validator;
    }

    @Override
    public String toString(){
        return "App [user = " + user + ", value = " + value +  "validator: " + validator + "]";
    }

    public String getUser() { return user;}
    public String getValue() { return value;}
    public String getValidator() {
        return validator;
    }

    public void setUser(String id) { this.user = id;}
    public void setValue(String a) { this.value = a;}
    public void setValidator(String validator) {
        this.validator = validator;
    }
}
