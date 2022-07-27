package com.example.Visma_Internship_Task.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Data {

    AtomicInteger seq = new AtomicInteger();

    private int id = seq.incrementAndGet();

    private String name;
    private String ResponsiblePerson;
    private String Description;
    private String Category;
    private String Type;
    private String StartDate;
    private String EndDate;

    private List<String> person = new ArrayList<>();

    public Data() {
    }

    public Data(String name, String responsiblePerson, String description, String category, String type, String startDate, String endDate) {
        this.name = name;
        ResponsiblePerson = responsiblePerson;
        Description = description;
        Category = category;
        Type = type;
        StartDate = startDate;
        EndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsiblePerson() {
        return ResponsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        ResponsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public List<String> getPerson() {
        return person;
    }

    public void setPerson(List<String> person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ResponsiblePerson='" + ResponsiblePerson + '\'' +
                ", Description='" + Description + '\'' +
                ", Category='" + Category + '\'' +
                ", Type='" + Type + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", person=" + person +
                '}';
    }
}
