package com.full_stack_project.flowable.modal;

import javax.persistence.*;

@Entity
@Table(name = "APP_LIST")
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NAME")
    private String NAME;
    @Column(name = "NEED_ADMIN")
    private int NEED_ADMIN;

    @Override
    public String toString(){
        return "App [name = " + NAME + ", need_admin = " + NEED_ADMIN + "]";
    }

    public String getNAME_() { return NAME;}
    public int getNEED_ADMIN() { return NEED_ADMIN;}

    public void setNAME_(String name) { this.NAME = name;}
    public void setPWD_(int na) { this.NEED_ADMIN = na;}
}
