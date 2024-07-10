package com.gabriel.smartclass.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankPosition{
    private String student;
    private Double final_grade;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public Double getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(Double final_grade) {
        this.final_grade = final_grade;
    }

    public RankPosition(String student, Double final_grade) {
        this.student = student;
        this.final_grade = final_grade;
    }

    public static List<RankPosition> make_ranking(List<RankPosition> list_to_order){
        Collections.sort(list_to_order, new Comparator<RankPosition>() {
            @Override
            public int compare(RankPosition r1, RankPosition r2) {
                return Double.compare(r2.getFinal_grade(), r1.getFinal_grade());
            }
        });
        return list_to_order;
    }

    @Override
    public String toString() {
        return "RankPosition{" +
                "student='" + student + '\'' +
                ", final_grade=" + final_grade +
                '}';
    }
}
