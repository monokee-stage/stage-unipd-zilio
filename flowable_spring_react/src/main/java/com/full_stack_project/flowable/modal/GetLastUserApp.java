package com.full_stack_project.flowable.modal;

import javax.persistence.*;

@Entity
@Table(name = "ACT_ID_USER_APP")
public class GetLastUserApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user")
    private String user;
    @Column(name = "value")
    private String value;

    public GetLastUserApp(){}

    public GetLastUserApp(String user, String value, int validated) {
        this.user = user;
        this.value = value;
    }

    @Override
    public String toString(){
        return "App [user = " + user + ", value = " + value + "]";
    }

    public String getUser() { return user;}
    public String getValue() { return value;}

    public void setUser(String id) { this.user = id;}
    public void setValue(String a) { this.value = a;}
}
