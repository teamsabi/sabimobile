package com.example.ngikngik.Dashboard;

public class item_name {
    private String name;
    private String className; // Ganti 'class' dengan 'className'

    public item_name(String name, String className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getClassName() { // Ganti nama metode dari 'getClass' ke 'getClassName'
        return className;
    }
}
