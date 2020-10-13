package com.full_stack_project.flowable.modal;

import javax.persistence.*;

@Entity
@Table(name = "ACT_RU_TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_")
    private int ID_;
    @Column(name = "NAME_")
    private String NAME_;
    @Column(name = "ASSIGNEE_")
    private String ASSIGNEE_;

    @Override
    public String toString(){
        return "Task [id = " + ID_ + ", name = " + NAME_ + "]";
    }

    public int getID_() { return ID_;}
    public String getNAME_() { return NAME_;}

    public String getASSIGNEE_() {
        return ASSIGNEE_;
    }

    public void setID_(int id) { this.ID_ = id;}
    public void setNAME_(String name) { this.NAME_ = name;}

    public void setASSIGNEE_(String ASSIGNEE_) {
        this.ASSIGNEE_ = ASSIGNEE_;
    }
}
