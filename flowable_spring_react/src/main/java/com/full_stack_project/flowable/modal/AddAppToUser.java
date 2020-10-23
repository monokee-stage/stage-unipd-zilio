package com.full_stack_project.flowable.modal;

import com.thoughtworks.qdox.model.expression.Add;

import javax.persistence.*;

@Entity
@Table(name = "ACT_ID_USER_APP")
public class AddAppToUser {
    @Id @GeneratedValue int id;
    @Column(name = "user")
    private String user;
    @Column(name = "value")
    private String value;
    @Column(name = "validated")
    private int validated;

    public AddAppToUser(){}

    public AddAppToUser(String user, String value){
        this.user = user;
        this.value = value;
    }

    public AddAppToUser(String user, String value, int validated) {
        this.user = user;
        this.value = value;
        this.validated = validated;
    }

    @Override
    public String toString(){
        return "App [user = " + user + ", value = " + value +  " validation: " + validated + "]";
    }
    public String getUser() { return user;}
    public String getValue() { return value;}
    public int getValidation() { return validated;}

    public void setUser(String id) { this.user = id;}
    public void setValue(String a) { this.value = a;}
    public void setValidated(int v) { this.validated = v;}
}
