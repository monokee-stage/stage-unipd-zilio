package com.full_stack_project.flowable.modal;

import javax.persistence.*;

@Entity
@Table(name = "ACT_ID_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_")
    private String ID_;
    @Column(name = "PWD_")
    private String PWD_;

    @Override
    public String toString(){
        return "User [id = " + ID_ + ", password = " + PWD_ + "]";
    }

    public String getID_() { return ID_;}
    public String getPWD_() { return PWD_;}

    public void setID_(String id) { this.ID_ = id;}
    public void setPWD_(String pwd) { this.PWD_ = pwd;}
}
