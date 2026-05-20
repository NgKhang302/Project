package com.khang.model;

import jakarta.persistence.*;

@Entity // class này map với bảng trong db
public class Category {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tự động sinh giá trị cho id khi insert record mới
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}